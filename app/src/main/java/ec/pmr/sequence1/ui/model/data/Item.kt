package ec.pmr.sequence1.ui.model.data

sealed class ListItem {
    data class Header(
        val imageRes: Int,
        val title: String,
        val subTitle: String,
        val description : String
    ) : ListItem()

    data class Item(
        val imageUrl: String,
        val title: String,
        val subTitle: String
    ) : ListItem()
}