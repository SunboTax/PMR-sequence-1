package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class Lists(
    @SerializedName("success")
    val success:Boolean,
    @SerializedName("lists")
    val lists: List<TodoList>
)
