package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("items")
    val items: List<TodoItem>
)
