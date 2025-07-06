package fr.khelp.zegaime.preferences

import fr.khelp.zegaime.database.Database
import fr.khelp.zegaime.database.type.DataType
import fr.khelp.zegaime.preferences.data.Preference
import fr.khelp.zegaime.preferences.data.PreferenceBoolean
import fr.khelp.zegaime.preferences.data.PreferenceDouble
import fr.khelp.zegaime.preferences.data.PreferenceFloat
import fr.khelp.zegaime.preferences.data.PreferenceInt
import fr.khelp.zegaime.preferences.data.PreferenceLong
import fr.khelp.zegaime.preferences.data.PreferenceString
import fr.khelp.zegaime.preferences.type.PreferenceType
import fr.khelp.zegaime.preferences.type.PreferenceTypeBoolean
import fr.khelp.zegaime.preferences.type.PreferenceTypeDouble
import fr.khelp.zegaime.preferences.type.PreferenceTypeFloat
import fr.khelp.zegaime.preferences.type.PreferenceTypeInt
import fr.khelp.zegaime.preferences.type.PreferenceTypeLong
import fr.khelp.zegaime.preferences.type.PreferenceTypeString
import fr.khelp.zegaime.utils.extensions.ifElse
import java.util.Optional

object PreferencesDatabase
{
    private const val NAME = "Name"
    private const val TYPE = "Type"
    private const val VALUE = "Value"

    private val database = Database.database("jhelp", "zegaime", "data/preferences")
    private val tablePreferences = this.database.table("Preferences") {
        NAME AS DataType.STRING
        TYPE AS DataType.STRING
        VALUE AS DataType.STRING
    }

    /** Preference cache */
    private val cache = HashMap<String, Preference<*, *>>()

    /**
     * Gets a Boolean preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The Boolean preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores a Boolean value
     */
    fun getBoolean(name : String) : PreferenceBoolean
    {
        return this.getPreference(name, false, PreferenceTypeBoolean) { name, value ->
            PreferenceBoolean(name, value)
        }
    }

    /**
     * Gets Int preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The Int preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores Int value
     */
    fun getInt(name : String) : PreferenceInt
    {
        return this.getPreference(name, 0, PreferenceTypeInt) { name, value ->
            PreferenceInt(name, value)
        }
    }

    /**
     * Gets Long preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The Long preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores Long value
     */
    fun getLong(name : String) : PreferenceLong
    {
        return this.getPreference(name, 0L, PreferenceTypeLong) { name, value ->
            PreferenceLong(name, value)
        }
    }

    /**
     * Gets Float preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The Float preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores Float value
     */
    fun getFloat(name : String) : PreferenceFloat
    {
        return this.getPreference(name, 0f, PreferenceTypeFloat) { name, value ->
            PreferenceFloat(name, value)
        }
    }

    /**
     * Gets Double preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The Double preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores Double value
     */
    fun getDouble(name : String) : PreferenceDouble
    {
        return this.getPreference(name, 0.0, PreferenceTypeDouble) { name, value ->
            PreferenceDouble(name, value)
        }
    }

    /**
     * Gets String preference
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     *
     * @return The String preference
     *
     * @throws IllegalArgumentException If preference name already exists but not stores String value
     */
    fun getString(name : String) : PreferenceString
    {
        return this.getPreference(name, "", PreferenceTypeString) { name, value ->
            PreferenceString(name, value)
        }
    }

    internal fun <T : Any, PT : PreferenceType<T>> update(preference : Preference<T, PT>)
    {
        this.tablePreferences.update {
            VALUE IS preference.type.serialize(preference.value)

            where { condition = NAME EQUALS preference.name }
        }
    }

    /**
     * Gets a preference by its name
     *
     * Note:
     * > If the preference does not exist, a new one is created with a default value
     *
     * @param name Preference name
     * @param defaultValue Default value if preference not already exists
     * @param preferenceType Expected preference type
     * @param newInstance Called if need creates a new instance
     *
     * @return The preference
     *
     * @throws IllegalArgumentException If preference name already exists but not expected type
     */
    private fun <T : Any, PT : PreferenceType<T>, P : Preference<T, PT>> getPreference(name : String,
                                                                                       defaultValue : T,
                                                                                       preferenceType : PT,
                                                                                       newInstance : (name : String, value : T) -> P) : P
    {
        /* Search preference in cache */
        var preference = cache[name]

        if (preference != null)
        {
            /* In cache, return it if good type */
            if (preference.type != preferenceType)
            {
                throw IllegalArgumentException("The preference $name is ${preference.type} not a $preferenceType")
            }

            @Suppress("UNCHECKED_CAST")
            return preference as P
        }

        /* Not in cache, search in database */
        val preferenceInfo = this.getPreferenceInfo(name)

        preference = preferenceInfo.ifElse({ (type, value) ->
                                               /* In database, use it if good type */
                                               if (type != preferenceType)
                                               {
                                                   throw IllegalArgumentException("The preference $name is $type not a ${preferenceType}")
                                               }

                                               newInstance(name, preferenceType.parse(value))
                                           }, {
                                               /* Not in database, creates a new instance */
                                               newInstance(name, defaultValue)
                                           })

        /* Store in cache */
        cache[name] = preference

        /* If not in database, store it */
        if (!preferenceInfo.isPresent)
        {
            this.store(preference)
        }

        return preference
    }

    /**
     * Queries preference information by its name
     *
     * @param name Preference name
     *
     * @return Preference information if stored in database
     */
    private fun getPreferenceInfo(name : String) : Optional<Pair<PreferenceType<*>, String>>
    {
        var result : Optional<Pair<PreferenceType<*>, String>> =
            Optional.empty<Pair<PreferenceType<*>, String>>()

        val cursor = this.tablePreferences.select {
            +TYPE
            +VALUE
            where { condition = NAME EQUALS name }
        }

        if (cursor.hasNext)
        {
            cursor.next {
                val typeName = getString(columnRange(TYPE))
                val valueSerialized = getString(columnRange(VALUE))

                for (preferenceTypeClass in PreferenceType::class.sealedSubclasses)
                {
                    if (typeName == preferenceTypeClass.simpleName)
                    {
                        val preferenceType = preferenceTypeClass.objectInstance as PreferenceType<*>
                        result = Optional.of<Pair<PreferenceType<*>, String>>(
                            Pair<PreferenceType<*>, String>(preferenceType, valueSerialized)
                                                                             )
                        break
                    }
                }
            }
        }

        cursor.close()

        return result
    }

    /**
     * Stores a preference in database
     *
     * @param preference Preference to store
     */
    private fun <T : Any, PT : PreferenceType<T>> store(preference : Preference<T, PT>)
    {
        this.tablePreferences.insert {
            NAME IS preference.name
            TYPE IS preference.type.javaClass.simpleName
            VALUE IS preference.type.serialize(preference.value)
        }
    }
}