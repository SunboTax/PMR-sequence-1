package ec.pmr.sequence1.data.db.dao

import androidx.room.*
import ec.pmr.sequence1.data.db.model.Itemdb

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(item: Itemdb)

    @Query("SELECT * FROM items WHERE listId = :listId")
    suspend fun getAllItems(listId: Int): List<Itemdb>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Int): Itemdb

    @Query("UPDATE items SET id = :id WHERE label= :label AND listId = :listId")
    suspend fun updateItemId(id: Int, label: String, listId: Int)

    @Query("UPDATE items SET checked = :checked WHERE id = :id")
    suspend fun changeItemState(id: Int, checked: String)

    @Query("UPDATE items SET updated = :updated WHERE id = :id")
    suspend fun changeItemUpdateState(id: Int, updated: Boolean)

    @Query("UPDATE items SET listId = :listId WHERE id = :id")
    suspend fun changeItemListId(id: Int, listId: Int)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteItem(id: Int)
}