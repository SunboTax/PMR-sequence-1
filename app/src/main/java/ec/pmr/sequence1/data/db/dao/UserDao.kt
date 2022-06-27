package ec.pmr.sequence1.data.db.dao

import androidx.room.*
import ec.pmr.sequence1.data.db.model.Userdb

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(user: Userdb)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<Userdb>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): Userdb?

    @Query("UPDATE users SET updated = :updated  WHERE username = :username")
    suspend fun updateUserState(username: String, updated: Boolean)
}