package dev.x001.foodies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.x001.foodies.R
import dev.x001.foodies.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expandableFloatingActionButton.setOnClickListener {
            onExpandableFABClicked()
        }

        binding.addRecipeFloatingActionButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add Recipe", Toast.LENGTH_SHORT).show()
        }

        binding.filterFloatingActionButton.setOnClickListener {
            Toast.makeText(requireContext(), "Filter", Toast.LENGTH_SHORT).show()
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
        _binding = null
    }
}