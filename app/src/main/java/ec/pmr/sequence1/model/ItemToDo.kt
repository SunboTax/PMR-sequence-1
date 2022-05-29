package ec.pmr.sequence1.model

import java.io.Serializable

class ItemToDo:Serializable {
    private lateinit var description:String
    private var fait:Boolean =false

    constructor(description: String) {
        this.description = description
    }

    constructor(description: String, fait: Boolean) {
        this.description = description
        this.fait = fait
    }

    fun getDescription():String{
        return this.description
    }

    fun setDescription(uneDescription: String){
        this.description = uneDescription
    }

    fun setFait(fait:Boolean){
        this.fait = fait
    }

    //inverse the state of the checkbox
    fun changeFait(){
        this.fait = !this.fait
    }

    fun getFait():Boolean{
        return this.fait
    }

    override fun toString(): String {
        return "ItemToDo(description=$description, fait=$fait)"
    }

}