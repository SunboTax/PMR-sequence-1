package ec.pmr.sequence1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.R
import ec.pmr.sequence1.data.api.TodoList

class ListAdapter(
    private val dataSet: ArrayList<TodoList>
) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    private var buttonListener:OnButtonClickListener? = null
    private var imageButtonListener:OnImageButtonClickListener? =null

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(itemView = itemView,buttonListener as OnButtonClickListener, imageButtonListener as OnImageButtonClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataSet[position].label)
    }

    //create a listener for every item in the recycle view
    interface OnButtonClickListener{
        fun onItemClick(position:Int)
    }

    interface OnImageButtonClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnButtonClickListener(listener:OnButtonClickListener){
        buttonListener = listener
    }

    fun setOnImageButtonClickListener(listener:OnImageButtonClickListener){
        imageButtonListener = listener
    }

    //refresh the screen when a new list is added
    fun addList(todoList: TodoList){
        dataSet.add(todoList)
        notifyItemChanged(dataSet.size)
    }

    fun deleteList(position: Int){
        dataSet.removeAt(position)
        for(it in 0..dataSet.size) {
            notifyItemChanged(it)
        }
    }

    class ItemViewHolder(itemView: View, listner1: OnButtonClickListener,listner2: OnImageButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val listTitre: Button = itemView.findViewById(R.id.btn_List_Choix)
        private val imageButton: ImageButton = itemView.findViewById(R.id.imageBtn_Choix)

        init {
            listTitre.setOnClickListener{
                listner1.onItemClick(adapterPosition)
            }
            imageButton.setOnClickListener{
                listner2.onItemClick(adapterPosition)
            }
        }

        fun bind(label: String) {
            listTitre.text = label
        }

    }
}

