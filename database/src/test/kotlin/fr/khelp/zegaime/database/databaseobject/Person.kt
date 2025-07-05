package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Database
import java.util.Objects

class Person(@PrimaryKey val name : String,
             age : Int,
             address : Address,
             database : Database) : DatabaseObject(database)
{
    var age : Int = age.coerceIn(0, 123)
        set(value)
        {
            field = value.coerceIn(0, 123)
            this.update()
        }

    var address : Address = address
        set(value)
        {
            field = value
            this.update()
        }

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other -> true
            null == other || other !is Person -> false
            else -> this.name == other.name && this.age == other.age && this.address == other.address
        }

    override fun hashCode() : Int =
        Objects.hash(this.name, this.age, this.address)

    override fun toString() : String =
        "${this.name} ${this.age} yeas old at ${this.address}"

}