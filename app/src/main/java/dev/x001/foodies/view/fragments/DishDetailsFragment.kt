package dev.x001.foodies.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dev.x001.foodies.R
import dev.x001.foodies.application.DishApplication
import dev.x001.foodies.databinding.FragmentAllDishesBinding
import dev.x001.foodies.databinding.FragmentDishDetailsBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.viewmodel.DishViewModel
import dev.x001.foodies.viewmodel.DishViewModelFactory

class DishDetailsFragment : Fragment() {

    private val args: DishDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentDishDetailsBinding

    private val mDishViewModel: DishViewModel by viewModels{
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }

    private var mDishDetails: Dish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("DISH", args.dishDetails.dish)

        binding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDishDetails = args.dishDetails
        menuSetup()

        binding.dishTextView.text = args.dishDetails.dish
        binding.categoryAndTypeTextView.text = "${args.dishDetails.category} · ${args.dishDetails.type}"
        binding.ingredientsTextView.text = args.dishDetails.ingredients

        var directions = ""
        if (args.dishDetails.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                directions = Html.fromHtml(
                    args.dishDetails.directionToCook,
                    Html.FROM_HTML_MODE_COMPACT
                ).toString()
            } else {
                @Suppress("DEPRECATION")
                directions = Html.fromHtml(args.dishDetails.directionToCook).toString()
            }
        }else{
            args.dishDetails.directionToCook
        }

        binding.directionsTextView.text = directions


            binding.cookingTimeTextView.text = "${args.dishDetails.cookingTime} minutes"

        Glide.with(requireContext())
            .load(args.dishDetails.image)
            .into(binding.dishImageView)

        //set heart icon
        if (args.dishDetails.favoriteDish){
            binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_selected
            ))
        }else{
            binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            ))
        }

        binding.ivFavoriteDish.setOnClickListener {
            //toggle the value on click
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

            mDishViewModel.update(args.dishDetails)

            if (args.dishDetails.favoriteDish){
                binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_selected
                ))
                Toast.makeText(requireActivity(), "Added to favorites!", Toast.LENGTH_SHORT).show()
            }else{
                binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_unselected
                ))
                Toast.makeText(requireActivity(), "Removed to favorites.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun menuSetup(){

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_share, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.actions_share_dish -> {
                        val type = "text/plain"
                        val subject = "Checkout this dish recipe."
                        var extraText = ""
                        val shareWith = "Share with"

                        mDishDetails?.let {
                            var image = ""
                            if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE){
                                image = it.image
                            }
                            var directions = ""
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                directions = Html.fromHtml(
                                    it.directionToCook,
                                    Html.FROM_HTML_MODE_COMPACT
                                ).toString()
                            } else {
                                @Suppress("DEPRECATION")
                                directions = Html.fromHtml(it.directionToCook).toString()
                            }

                            extraText =
                                "$image \n" +
                                        "\n Title: ${it.dish} \n\n Type: ${it.type} \n\n " +
                                        "Category: ${it.category} " +
                                        "\n\n Ingredients \n ${it.ingredients} \n\n Instructions " +
                                        "To Cook: \n $directions" +
                                        "\n\n Time required to cook the dish approx " +
                                        "${it.cookingTime} minutes."
                        }
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = type
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                        intent.putExtra(Intent.EXTRA_TEXT, extraText)
                        startActivity(Intent.createChooser(intent, shareWith))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}