package dev.x001.foodies.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dev.x001.foodies.R
import dev.x001.foodies.databinding.FragmentAllDishesBinding
import dev.x001.foodies.databinding.FragmentDishDetailsBinding

class DishDetailsFragment : Fragment() {

    private val args: DishDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentDishDetailsBinding

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

        binding.dishTextView.text = args.dishDetails.dish
        binding.categoryAndTypeTextView.text = "${args.dishDetails.category} · ${args.dishDetails.type}"
        binding.ingredientsTextView.text = args.dishDetails.ingredients
        binding.directionsTextView.text = args.dishDetails.directionToCook
        binding.cookingTimeTextView.text = "${args.dishDetails.cookingTime} minutes"

        Glide.with(requireContext())
            .load(args.dishDetails.image)
            .into(binding.dishImageView)
    }

}