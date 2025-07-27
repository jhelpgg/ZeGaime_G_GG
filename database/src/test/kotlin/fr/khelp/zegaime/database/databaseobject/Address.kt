package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Database
import java.util.Objects

class Address(@PrimaryKey val street : String,
              @PrimaryKey val number : Int,
              database : Database) : DatabaseObject(database)
{
    override fun toString() : String =
        "$number $street"

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                     -> true
            null == other || other !is Address -> false
            else                               -> this.street == other.street && this.number == other.number
        }

    override fun hashCode() : Int =
        Objects.hash(this.street, this.number)
}