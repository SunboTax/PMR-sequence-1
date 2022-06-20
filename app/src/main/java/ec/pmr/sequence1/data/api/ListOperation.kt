package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class ListOperation(
    @SerializedName("success")
    val success:Boolean,
    @SerializedName("list")
    val todoList: TodoList
)
