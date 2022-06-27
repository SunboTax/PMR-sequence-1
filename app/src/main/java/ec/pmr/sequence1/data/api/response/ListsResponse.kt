package ec.pmr.sequence1.data.api.response

import ec.pmr.sequence1.data.api.model.TodoList
import kotlin.collections.List

data class ListsResponse(
    val success:Boolean,
    val lists: List<TodoList>
)
