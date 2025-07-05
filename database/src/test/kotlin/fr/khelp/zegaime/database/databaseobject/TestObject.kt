package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Database

class TestObject(@PrimaryKey val name : String,
                 age : Int = 0,
                 database : Database) : DatabaseObject(database)
{
    var age : Int = age.coerceIn(0, 123)
        set(value)
        {
            field = value.coerceIn(0, 123)
            this.update()
        }
}