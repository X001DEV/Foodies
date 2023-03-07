package dev.x001.foodies.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import dev.x001.foodies.R

class AddUpdateDishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_dish)

        Animatoo.animateSlideLeft(this)
    }
}