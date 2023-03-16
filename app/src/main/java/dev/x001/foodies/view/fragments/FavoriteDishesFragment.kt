package dev.x001.foodies.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.x001.foodies.application.DishApplication
import dev.x001.foodies.databinding.FragmentFavoriteDishesBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.view.activities.MainActivity
import dev.x001.foodies.view.adapter.DishAdapter
import dev.x001.foodies.viewmodel.DashboardViewModel
import dev.x001.foodies.viewmodel.DishViewModel
import dev.x001.foodies.viewmodel.DishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var _binding: FragmentFavoriteDishesBinding? = null
    private val binding get() = _binding!!

    private val mDishViewModel: DishViewModel by viewModels{
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dishAdapter = DishAdapter(this@FavoriteDishesFragment)
        binding.favoriteDishRecyclerView.adapter = dishAdapter

        mDishViewModel.favoriteDishes.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
                if (it.isNotEmpty()){
                    for(dish in it){
                        dishAdapter.dishesList(it)

                        binding.favoriteDishRecyclerView.visibility = View.VISIBLE
                        binding.textFavoriteTextView.visibility = View.GONE
                    }
                }else{
                    binding.favoriteDishRecyclerView.visibility = View.GONE
                    binding.textFavoriteTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    fun favoriteDishDetails(dish: Dish){
        findNavController().navigate(FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToDishDetailsFragment(
            dish
        ))
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}