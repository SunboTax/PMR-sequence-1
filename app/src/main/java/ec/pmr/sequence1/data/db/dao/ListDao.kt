package ec.pmr.sequence1.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ec.pmr.sequence1.data.db.model.Listdb
import retrofit2.http.DELETE

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(list: Listdb)

    @Query("SELECT * FROM lists")
    suspend fun getAllLists(): List<Listdb>

    @Query("SELECT * FROM lists WHERE id = :id")
    suspend fun getList(id: Int): Listdb

    @Query("SELECT * FROM lists WHERE username = :username")
    suspend fun getAllLists(username: String): List<Listdb>

    @Query("UPDATE lists SET id = :id WHERE label = :label")
    suspend fun updateListId(id:Int, label: String)

    @Query("UPDATE lists SET updated = :updated  WHERE id = :id")
    suspend fun updateListState(id: Int, updated: Boolean)

    @Query("DELETE From lists WHERE label = :label")
    suspend fun deleteList(label: String)
}