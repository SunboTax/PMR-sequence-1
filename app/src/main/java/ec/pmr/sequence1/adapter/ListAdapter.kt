package ec.pmr.sequence1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.R
import ec.pmr.sequence1.model.ListeToDo

class ListAdapter(
    private val dataSet: ArrayList<ListeToDo>
) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    var buttonListener:onButtonClickListener? = null

    override fun getItemCount(): Int {
        val int = dataSet.size
        return int
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(itemView = itemView,buttonListener as onButtonClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataSet[position].getTitreListeToDo())
    }

    interface onButtonClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnButtonClickListener(listner:onButtonClickListener){
        buttonListener = listner
    }

    fun addList(titreList: String){
        dataSet.add(ListeToDo(titreList))
        notifyItemChanged(dataSet.size)
    }

    class ItemViewHolder(itemView: View, listner: onButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val listTitre: Button = itemView.findViewById<Button>(R.id.btn_List_Choix)

        init {
            listTitre.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    listner.onItemClick(adapterPosition)
                }
            })
        }

        fun bind(item: String) {
            listTitre.text = item
        }

    }
}

