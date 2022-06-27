package ec.pmr.sequence1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ec.pmr.sequence1.data.db.dao.ItemDao
import ec.pmr.sequence1.data.db.dao.ListDao
import ec.pmr.sequence1.data.db.dao.UserDao
import ec.pmr.sequence1.data.db.model.Itemdb
import ec.pmr.sequence1.data.db.model.Listdb
import ec.pmr.sequence1.data.db.model.Userdb

@Database(entities = [Userdb::class, Listdb::class, Itemdb::class],version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun listDao(): ListDao
    abstract fun itemDao(): ItemDao
}