package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class ItemOperation(
    @SerializedName("success")
    val success:Boolean,
    @SerializedName("item")
    val todoItem: TodoItem
)
