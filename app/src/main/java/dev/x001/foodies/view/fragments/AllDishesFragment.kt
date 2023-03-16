package dev.x001.foodies.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.x001.foodies.R
import dev.x001.foodies.application.DishApplication
import dev.x001.foodies.databinding.DialogListBinding
import dev.x001.foodies.databinding.FragmentAllDishesBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.view.activities.AddUpdateDishActivity
import dev.x001.foodies.view.activities.MainActivity
import dev.x001.foodies.view.adapter.DishAdapter
import dev.x001.foodies.view.adapter.ListItemAdapter
import dev.x001.foodies.viewmodel.DishViewModel
import dev.x001.foodies.viewmodel.DishViewModelFactory

class AllDishesFragment : Fragment() {

    private lateinit var binding: FragmentAllDishesBinding

    private val mDishViewModel: DishViewModel by viewModels{
        DishViewModelFactory((requireActivity().application as DishApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dishAdapter = DishAdapter(this@AllDishesFragment)
        binding.dishRecyclerView.adapter = dishAdapter

        mDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
                Toast.makeText(requireActivity(), "DATA IS UPDATED", Toast.LENGTH_SHORT).show()
               if (it.isNotEmpty()){
                   dishAdapter.dishesList(it)

                   binding.dishRecyclerView.visibility = View.VISIBLE
                   binding.textNoDataTextView.visibility = View.GONE
               }else{
                   binding.dishRecyclerView.visibility = View.INVISIBLE
                   binding.textNoDataTextView.visibility = View.VISIBLE
               }
            }
        }

        binding.expandableFloatingActionButton.setOnClickListener {
            onExpandableFABClicked()
        }

        binding.addRecipeFloatingActionButton.setOnClickListener {
            startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
        }

        binding.filterFloatingActionButton.setOnClickListener {
            filterDishesListDialog()
        }
    }

    fun dishDetails(dish: Dish){
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToDishDetailsFragment(
            dish
        ))
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    fun deleteDish(dish: Dish){
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("Delete Dish")
        dialog.setMessage("Are you sure you want to delete ${dish.dish}?")
        dialog.setIcon(R.drawable.ic_baseline_delete_24)
        dialog.setPositiveButton("Yes"){
            dialogInterface, _ ->
            mDishViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        dialog.setNegativeButton("Cancel"){
                dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.create()
        dialog.show()
    }

    private fun filterDishesListDialog(){
        val listDialog = Dialog(requireActivity())
        val dialogBinding: DialogListBinding = DialogListBinding.inflate(layoutInflater)
        listDialog.setContentView(dialogBinding.root)

        dialogBinding.titleTextView.text = "Select item filter"
        val dishTypes = Constants.dishTypes
        dishTypes.add(0, Constants.ALL_ITEMS)

        val adapter = ListItemAdapter(requireActivity(), dishTypes, Constants.FILTER_SELECTION)

        dialogBinding.listRecyclerView.adapter = adapter
        listDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    private var fabClicked = false
    private fun onExpandableFABClicked() {
        setFABVisibility(fabClicked)
        setFABAnimation(fabClicked)

        fabClicked = !fabClicked
    }

    private fun setFABAnimation(fabClicked: Boolean) {
        val rotateOpen: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim)
        val rotateClose: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim)
        val fromBottom: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim)
        val toBottom: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim)

        if (!fabClicked){
            binding.addRecipeFloatingActionButton.startAnimation(fromBottom)
            binding.filterFloatingActionButton.startAnimation(fromBottom)
            binding.expandableFloatingActionButton.startAnimation(rotateOpen)
        }else{
            binding.addRecipeFloatingActionButton.startAnimation(toBottom)
            binding.filterFloatingActionButton.startAnimation(toBottom)
            binding.expandableFloatingActionButton.startAnimation(rotateClose)
        }
    }

    private fun setFABVisibility(fabClicked: Boolean) {
        if (!fabClicked){
            binding.addRecipeFloatingActionButton.visibility = View.VISIBLE
            binding.filterFloatingActionButton.visibility = View.VISIBLE
            binding.addRecipeFloatingActionButton.isClickable = true
            binding.filterFloatingActionButton.isClickable = true
        }else{
            binding.addRecipeFloatingActionButton.visibility = View.GONE
            binding.filterFloatingActionButton.visibility = View.GONE
            binding.addRecipeFloatingActionButton.isClickable = false
            binding.filterFloatingActionButton.isClickable = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}