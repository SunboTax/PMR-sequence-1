package ec.pmr.sequence1.model

import java.io.Serializable

class ItemToDo:Serializable {
    private var description:String? = null
    private var fait:Boolean =false

    constructor()
    constructor(description: String?) {
        this.description = description
    }

    constructor(description: String?, fait: Boolean) {
        this.description = description
        this.fait = fait
    }

    fun getDescription():String{
        return this.description as String
    }

    fun setDiscription(uneDescription: String){
        this.description = uneDescription
    }

    fun setFait(fait:Boolean){
        this.fait = fait
    }

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