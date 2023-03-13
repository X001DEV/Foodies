package dev.x001.foodies.application

import android.app.Application
import dev.x001.foodies.model.database.DishRepository
import dev.x001.foodies.model.database.DishRoomDatabase

class DishApplication: Application() {
    private val database by lazy {
        DishRoomDatabase.getDatabase(this)
    }

    val repository by lazy {
        DishRepository(database.dishDao())
    }
}