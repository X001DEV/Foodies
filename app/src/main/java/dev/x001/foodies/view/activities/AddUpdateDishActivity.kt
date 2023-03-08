package dev.x001.foodies.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import dev.x001.foodies.R
import dev.x001.foodies.databinding.ActivityAddUpdateDishBinding

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
                    Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}