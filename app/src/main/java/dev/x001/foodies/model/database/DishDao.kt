package dev.x001.foodies.model.database

import androidx.room.Dao
import androidx.room.Insert
import dev.x001.foodies.model.entities.Dish

@Dao
interface DishDao {

    @Insert
    suspend fun insertDishDetails(dish: Dish)
}