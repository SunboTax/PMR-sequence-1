package ec.pmr.sequence1.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Itemdb(
    @PrimaryKey
    val id: Int,
    val listId:Int,
    val label: String,
    var checked: String,
    var updated: Boolean
)