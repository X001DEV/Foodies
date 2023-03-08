package dev.x001.foodies.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import dev.x001.foodies.R
import dev.x001.foodies.databinding.ActivityAddUpdateDishBinding
import dev.x001.foodies.databinding.DialogImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addImageImageView.setOnClickListener(this)

        Animatoo.animateSlideLeft(this)
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.add_image_imageView -> {
                    dialogSelectImage()
                }
            }
        }
    }

    private fun dialogSelectImage(){
        val dialog = Dialog(this)
        val dialogBinding : DialogImageSelectionBinding = DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.cameraLinearLayout.setOnClickListener {
            Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.galleryLinearLayout.setOnClickListener {
            Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}