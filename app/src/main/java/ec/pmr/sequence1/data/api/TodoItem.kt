package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class TodoItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("label")
    val label: String,
    @SerializedName("checked")
    var isChecked: String
)
