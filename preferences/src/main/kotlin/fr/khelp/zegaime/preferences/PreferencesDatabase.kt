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

/**
 * A database for storing preferences.
 *
 * This object provides methods to get and set preferences of different types.
 * The preferences are stored in a database, so they are persisted between application launches.
 */
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
     * Gets a boolean preference.
     *
     * If the preference does not exist, a new one is created with a default value of `false`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getBoolean("myPreference")
     * myPreference.value = true
     * ```
     *
     * @param name The name of the preference.
     * @return The boolean preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not a boolean.
     */
    fun getBoolean(name : String) : PreferenceBoolean
    {
        return this.getPreference(name, false, PreferenceTypeBoolean) { preferenceName, value ->
            PreferenceBoolean(preferenceName, value)
        }
    }

    /**
     * Gets an integer preference.
     *
     * If the preference does not exist, a new one is created with a default value of `0`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getInt("myPreference")
     * myPreference.value = 1
     * ```
     *
     * @param name The name of the preference.
     * @return The integer preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not an integer.
     */
    fun getInt(name : String) : PreferenceInt
    {
        return this.getPreference(name, 0, PreferenceTypeInt) { preferenceName, value ->
            PreferenceInt(preferenceName, value)
        }
    }

    /**
     * Gets a long preference.
     *
     * If the preference does not exist, a new one is created with a default value of `0L`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getLong("myPreference")
     * myPreference.value = 1L
     * ```
     *
     * @param name The name of the preference.
     * @return The long preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not a long.
     */
    fun getLong(name : String) : PreferenceLong
    {
        return this.getPreference(name, 0L, PreferenceTypeLong) { preferenceName, value ->
            PreferenceLong(preferenceName, value)
        }
    }

    /**
     * Gets a float preference.
     *
     * If the preference does not exist, a new one is created with a default value of `0.0f`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getFloat("myPreference")
     * myPreference.value = 1.0f
     * ```
     *
     * @param name The name of the preference.
     * @return The float preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not a float.
     */
    fun getFloat(name : String) : PreferenceFloat
    {
        return this.getPreference(name, 0f, PreferenceTypeFloat) { preferenceName, value ->
            PreferenceFloat(preferenceName, value)
        }
    }

    /**
     * Gets a double preference.
     *
     * If the preference does not exist, a new one is created with a default value of `0.0`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getDouble("myPreference")
     * myPreference.value = 1.0
     * ```
     *
     * @param name The name of the preference.
     * @return The double preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not a double.
     */
    fun getDouble(name : String) : PreferenceDouble
    {
        return this.getPreference(name, 0.0, PreferenceTypeDouble) { preferenceName, value ->
            PreferenceDouble(preferenceName, value)
        }
    }

    /**
     * Gets a string preference.
     *
     * If the preference does not exist, a new one is created with a default value of `""`.
     *
     * **Usage example**
     * ```kotlin
     * val myPreference = PreferencesDatabase.getString("myPreference")
     * myPreference.value = "hello"
     * ```
     *
     * @param name The name of the preference.
     * @return The string preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but is not a string.
     */
    fun getString(name : String) : PreferenceString
    {
        return this.getPreference(name, "", PreferenceTypeString) { preferenceName, value ->
            PreferenceString(preferenceName, value)
        }
    }

    fun close()
    {
        this.database.close()
    }

    /**
     * Updates a preference in the database.
     *
     * @param preference The preference to update.
     *
     */
    internal fun <T : Any, PT : PreferenceType<T>> update(preference : Preference<T, PT>)
    {
        this.tablePreferences.update {
            VALUE IS preference.type.serialize(preference.value)

            where { condition = NAME EQUALS preference.name }
        }
    }

    /**
     * Gets a preference by its name.
     *
     * If the preference does not exist, a new one is created with the given default value.
     *
     * @param name The name of the preference.
     * @param defaultValue The default value if the preference does not already exist.
     * @param preferenceType The expected type of the preference.
     * @param newInstance A function to create a new instance of the preference.
     * @return The preference.
     * @throws IllegalArgumentException If a preference with the same name already exists but has a different type.
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
     * Queries preference information by its name.
     *
     * @param name The name of the preference.
     * @return The preference information if stored in the database.
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
     * Stores a preference in the database.
     *
     * @param preference The preference to store.
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
