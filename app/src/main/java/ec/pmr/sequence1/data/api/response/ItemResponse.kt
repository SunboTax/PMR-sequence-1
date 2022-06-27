package ec.pmr.sequence1.data.api.response

import ec.pmr.sequence1.data.api.model.TodoItem

data class ItemResponse(
    val success:Boolean,
    val item: TodoItem
)
