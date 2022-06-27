package ec.pmr.sequence1.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Update

@Entity(tableName = "users")
data class Userdb (
    @PrimaryKey
    val username:String,
    var password:String,
    var token:String,
    var updated:Boolean
)