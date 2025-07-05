package fr.khelp.zegaime.database.databaseobject

import fr.khelp.zegaime.database.Database

class TestObject(var name : String = "",
                 var age : Int = 0,
                 database : Database) : DatabaseObject(database)