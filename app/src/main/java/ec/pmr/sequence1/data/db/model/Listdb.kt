package ec.pmr.sequence1.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class Listdb(
    @PrimaryKey
    val id: Int,
    val label: String,
    val username: String,
    var updated: Boolean
    )