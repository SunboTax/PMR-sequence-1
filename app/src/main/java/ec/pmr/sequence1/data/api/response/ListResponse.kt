package ec.pmr.sequence1.data.api.response

import ec.pmr.sequence1.data.api.model.TodoList

data class ListResponse(
    val success:Boolean,
    val list: TodoList
)
