package dev.x001.foodies.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.x001.foodies.R
import dev.x001.foodies.databinding.ItemDishLayoutBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.view.activities.AddUpdateDishActivity
import dev.x001.foodies.view.fragments.AllDishesFragment
import dev.x001.foodies.view.fragments.FavoriteDishesFragment

class DishAdapter(private val fragment: Fragment): RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    private var dishes: List<Dish> = listOf()

    class ViewHolder(view: ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root) {
        val imageImageView = view.dishImageView
        val dishTextView = view.dishTextView
        val moreImageView = view.moreImageView
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
            when (fragment) {
                is FavoriteDishesFragment -> {
                    fragment.favoriteDishDetails(dish)
                }
                is AllDishesFragment -> {
                    fragment.dishDetails(dish)
                }
            }
        }

        holder.moreImageView.setOnClickListener {
            val popUp = PopupMenu(fragment.context, holder.moreImageView)
            popUp.menuInflater.inflate(R.menu.adapter_menu, popUp.menu)

            popUp.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish){
                    val intent = Intent(fragment.requireContext(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)

                }else if (it.itemId == R.id.action_delete_dish){
                    if (fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                }
                true
            }
            popUp.show()
        }

        if (fragment is AllDishesFragment){
            holder.moreImageView.visibility = View.VISIBLE
        }else if (fragment is FavoriteDishesFragment){
            holder.moreImageView.visibility = View.GONE
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