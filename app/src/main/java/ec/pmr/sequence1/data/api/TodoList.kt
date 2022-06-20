package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class TodoList(
    @SerializedName("id")
    val id : Int,
    @SerializedName("label")
    val label : String
)