package ec.pmr.sequence1.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ListeToDo :Serializable {
    private lateinit var titreListeTodo:String
    private lateinit var lesItems:ArrayList<ItemToDo>

    constructor(titreListeTodo: String) {
        this.titreListeTodo = titreListeTodo
        this.lesItems = arrayListOf()
    }


    fun setTitreListeToDo(titreListeToDo: String){
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
        this.lesItems?.add(itemToDo)
    }

    fun inLesItems(item: String):Boolean{
        for(it in this.lesItems){
            if(it.getDescription() == item){
                return true
            }
        }
        return false
    }

    fun rechercherItem(descriptionItem:String):ItemToDo{
        val lesItems:ArrayList<ItemToDo> = this.lesItems as ArrayList<ItemToDo>
        var itemToDo:ItemToDo? = null
        for(it in lesItems){
            if(it.getDescription() == descriptionItem){
                itemToDo = it
                break
            }
        }
        return itemToDo as ItemToDo
    }

    override fun toString(): String {
        return "ListeToDo(titreListeTodo=$titreListeTodo, lesItems=${lesItems?.toString()})"
    }

}