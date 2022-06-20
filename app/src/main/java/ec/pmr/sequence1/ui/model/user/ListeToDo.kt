package ec.pmr.sequence1.ui.model.user

import java.io.Serializable
import kotlin.collections.ArrayList

class ListeToDo(private var titreListeTodo: String) :Serializable {
    private lateinit var lesItems:ArrayList<ItemToDo>

    init {
        this.lesItems = arrayListOf()
    }

    fun setTitreListeToDo(titreListeTodo: String){
        this.titreListeTodo = titreListeTodo
    }

    fun getTitreListeToDo():String{
        return this.titreListeTodo as String
    }

    fun setLesItems(lesItems:ArrayList<ItemToDo>){
        this.lesItems = lesItems
    }

    fun getLesItems():ArrayList<ItemToDo>{
        return this.lesItems as ArrayList<ItemToDo>
    }

    fun ajouteItem(itemToDo: ItemToDo){
        this.lesItems.add(itemToDo)
    }

    //judge if an item is in the list
    fun inLesItems(descriptionItem: String):Boolean{
        for(it in this.lesItems){
            if(it.getDescription() == descriptionItem){
                return true
            }
        }
        return false
    }

    fun rechercherItem(descriptionItem:String): ItemToDo {
        val lesItems:ArrayList<ItemToDo> = this.lesItems as ArrayList<ItemToDo>
        var itemToDo: ItemToDo? = null
        for(it in lesItems){
            if(it.getDescription() == descriptionItem){
                itemToDo = it
                break
            }
        }
        return itemToDo as ItemToDo
    }

    override fun toString(): String {
        return "ListeToDo(titreListeTodo=$titreListeTodo, lesItems=${lesItems.toString()})"
    }

}