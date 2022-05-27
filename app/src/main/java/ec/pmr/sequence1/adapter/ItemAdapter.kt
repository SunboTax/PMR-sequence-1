package ec.pmr.sequence1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.R
import ec.pmr.sequence1.model.ItemToDo

class ItemAdapter (
    private val dataSet: ArrayList<ItemToDo>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var checkBoxListener: ItemAdapter.onChekcBoxClickListener? = null

    override fun getItemCount(): Int {
        val int = dataSet.size
        return int
    }

    interface onChekcBoxClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnCheckBoxClickListener(listner: ItemAdapter.onChekcBoxClickListener){
        checkBoxListener = listner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_switch, parent, false)
        return ItemViewHolder(itemView = itemView, checkBoxListener as onChekcBoxClickListener )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(item = dataSet[position])
    }

    fun addItem(item: String){
        dataSet.plus(ItemToDo(item))
        Log.d("itemAdapter",dataSet.size.toString())
        notifyItemChanged(dataSet.size)
    }

    class ItemViewHolder(itemView: View,listen:onChekcBoxClickListener) : RecyclerView.ViewHolder(itemView) {
        val itemTitre = itemView.findViewById<CheckBox>(R.id.switch_CheckBox_Show)

        init{
            itemTitre.setOnClickListener{
                listen.onItemClick(adapterPosition)
            }
        }

        fun bind(item: ItemToDo) {
            itemTitre.text = item.getDescription()
            itemTitre.isChecked = item.getFait()
        }
    }
}

