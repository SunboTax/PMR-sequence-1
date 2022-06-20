package ec.pmr.sequence1.ui.model.user

import java.io.Serializable

class ProfilListeTodo :Serializable {
    private lateinit var login:String
    private lateinit var mesListeToDo:ArrayList<ListeToDo>

    constructor(){}

    constructor(login: String) {
        this.login = login
        this.mesListeToDo = arrayListOf()
    }

    constructor(login: String, mesListeToDo: ArrayList<ListeToDo>) {
        this.login = login
        this.mesListeToDo = mesListeToDo
    }

    fun getMesListeToDo():ArrayList<ListeToDo>?{
        return this.mesListeToDo
    }

    fun setMesListeToDo(mesListeToDo: ArrayList<ListeToDo>){
        for(it in mesListeToDo){
            this.mesListeToDo?.plus(it)
        }
    }

    fun ajouteListe(uneListe: ListeToDo){
        this.mesListeToDo.plus(uneListe)
    }

    //judge if a list is one of the lists of the user
    fun inMesListeToDo(titreList: String):Boolean{
        for(it in this.mesListeToDo){
            if(it.getTitreListeToDo() == titreList){
                return true
            }
        }
        return false
    }

    //refresh the information stored
    fun refreshList(listeToDo: ListeToDo){
        for(it in 0..this.mesListeToDo.size){
            if (this.mesListeToDo[it].getTitreListeToDo() == listeToDo.getTitreListeToDo()){
                this.mesListeToDo[it].setLesItems(listeToDo.getLesItems())
                break
            }
        }
    }

    fun getLogin():String{
        return this.login as String
    }

    fun setLogin(unLogin:String){
        this.login = unLogin
    }

    override fun toString(): String {
        return "ProfilListeTodo(login=$login," +'\n'+ "mesListeToDo=${mesListeToDo?.toString()})"
    }
}