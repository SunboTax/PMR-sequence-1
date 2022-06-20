package ec.pmr.sequence1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.R
import ec.pmr.sequence1.data.api.TodoItem

class ItemAdapter (
    val dataSet: ArrayList<TodoItem>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var checkBoxListener: ItemAdapter.OnCheckBoxClickListener? = null
    private var imageButtonListener:ItemAdapter.OnButtonClickListener? =null

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface OnCheckBoxClickListener{
        fun onItemClick(position:Int)
    }

    interface OnButtonClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnCheckBoxClickListener(listener: ItemAdapter.OnCheckBoxClickListener){
        checkBoxListener = listener
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener){
        imageButtonListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_switch, parent, false)
        return ItemViewHolder(itemView = itemView, checkBoxListener as OnCheckBoxClickListener, imageButtonListener as OnButtonClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(item = dataSet[position])
    }

    fun addItem(item: TodoItem){
        dataSet.add(item)
        notifyItemChanged(dataSet.size)
    }

    fun deleteItem(position: Int){
        dataSet.removeAt(position)
        for(it in 0..dataSet.size) {
            notifyItemChanged(it)
        }
    }

    class ItemViewHolder(itemView: View,listen1:OnCheckBoxClickListener,listen2:OnButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val itemTitre: CheckBox = itemView.findViewById(R.id.ckb_Item_Show)
        private val imageButton: ImageButton = itemView.findViewById(R.id.imageBtn_Show)

        init {
            itemTitre.setOnClickListener {
                listen1.onItemClick(adapterPosition)
            }
            imageButton.setOnClickListener{
                listen2.onItemClick(adapterPosition)
            }
        }

        fun bind(item: TodoItem) {
            itemTitre.text = item.label
            itemTitre.isChecked = item.isChecked == "1"
        }
    }
}

