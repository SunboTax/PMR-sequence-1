package ec.pmr.sequence1.data.api.response

import ec.pmr.sequence1.data.api.model.TodoItem
import kotlin.collections.List

data class ItemsResponse(
    val success: Boolean,
    val items: List<TodoItem>
)
