package ec.pmr.sequence1.data.api.model

import com.google.gson.annotations.SerializedName

data class TodoItem(
    val id: Int,
    val label: String,
    var checked: String
)
