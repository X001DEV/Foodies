package dev.x001.foodies.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.x001.foodies.databinding.ItemListBinding
import dev.x001.foodies.view.activities.AddUpdateDishActivity

class ListItemAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val selection: String
    ): RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        val textTextView = binding.text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemListBinding = ItemListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.textTextView.text = item

        holder.itemView.setOnClickListener {
            if (activity is AddUpdateDishActivity){
                activity.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}