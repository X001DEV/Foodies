package dev.x001.foodies.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.x001.foodies.databinding.ItemDishLayoutBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.view.fragments.AllDishesFragment

class DishAdapter(private val fragment: Fragment): RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    private var dishes: List<Dish> = listOf()

    class ViewHolder(view: ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root) {
        val imageImageView = view.dishImageView
        val dishTextView = view.dishTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.imageImageView)
        holder.dishTextView.text = dish.dish

        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list: List<Dish>){
        dishes = list
        notifyDataSetChanged()
    }


}