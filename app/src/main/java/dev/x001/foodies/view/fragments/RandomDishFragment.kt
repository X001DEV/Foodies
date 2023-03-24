package dev.x001.foodies.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dev.x001.foodies.R
import dev.x001.foodies.application.DishApplication
import dev.x001.foodies.databinding.FragmentRandomDishBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.model.entities.RandomDish
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.utils.capitalizeFirstLetter
import dev.x001.foodies.viewmodel.DishViewModel
import dev.x001.foodies.viewmodel.DishViewModelFactory
import dev.x001.foodies.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private lateinit var binding: FragmentRandomDishBinding

    private lateinit var mRandomDishViewModel: RandomDishViewModel

    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_loading)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.setDimAmount(0F)
            it.show()
        }
    }

    private fun hideProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeAPI()

        randomDishViewModelObserver()

        binding.randomDishSwipeRefreshLayout.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeAPI()

            randomDishViewModelObserver()
        }
    }

    private fun randomDishViewModelObserver() {

        val dialog = Dialog(requireActivity())
        dialog.let {
            it.setContentView(R.layout.dialog_loading)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.setDimAmount(0F)
            it.setCancelable(false)
            it.show()

            mRandomDishViewModel.randomDishResponse.observe(
                viewLifecycleOwner
            ) { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")
                    if (binding.randomDishSwipeRefreshLayout.isRefreshing) {
                        binding.randomDishSwipeRefreshLayout.isRefreshing = false
                    }
                    setRandomDishResponseInUI(randomDishResponse.recipes[0])
                }
            }

            mRandomDishViewModel.randomDishLoadingError.observe(
                viewLifecycleOwner
            ) { dataError ->
                dataError.let {
                    Log.i("Random Dish API Error", "$dataError")
                    if (binding.randomDishSwipeRefreshLayout.isRefreshing) {
                        binding.randomDishSwipeRefreshLayout.isRefreshing = false
                    }
                }
            }
            mRandomDishViewModel.loadRandomDish.observe(
                viewLifecycleOwner
            ) { loadRandomDish ->
                loadRandomDish?.let {
                    Log.i("Random Dish Loading", "$loadRandomDish")
                    if (!loadRandomDish) {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe){
        Glide.with(requireActivity())
            .load(recipe.image)
            .into(binding.dishImageView)

        binding.dishTextView.text = recipe.title

        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            binding.categoryAndTypeTextView.text = "${dishType.capitalizeFirstLetter()} · Other"
        }

        var ingredients = ""
        for (value in recipe.extendedIngredients){
            ingredients = if (ingredients.isEmpty()){
                value.original
            } else {
                ingredients + ", \n" + value.original
            }
        }
        binding.ingredientsTextView.text = ingredients

        var directions = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            directions = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            ).toString()
        } else {
            @Suppress("DEPRECATION")
            directions = Html.fromHtml(recipe.instructions).toString()
        }

        binding.directionsTextView.text = directions

        var addedToFavorites = false

        binding.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        binding.cookingTimeTextView.text = recipe.readyInMinutes.toString() + " minutes"

        binding.ivFavoriteDish.setOnClickListener {

            if (addedToFavorites){
                Toast.makeText(requireActivity(), "Already dded to favorites.", Toast.LENGTH_SHORT).show()
            } else {
                addedToFavorites = true
                val dishViewModel: DishViewModel by viewModels {
                    DishViewModelFactory((requireActivity(). application as DishApplication).repository)
                }

                val randomDishDetails = Dish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType.capitalizeFirstLetter(),
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

                dishViewModel.insert(randomDishDetails)

                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(requireActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}