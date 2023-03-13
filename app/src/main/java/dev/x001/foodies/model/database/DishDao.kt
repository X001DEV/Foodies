package dev.x001.foodies.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.x001.foodies.model.entities.Dish
import kotlinx.coroutines.flow.Flow


@Dao
interface DishDao {

    @Insert
    suspend fun insertDishDetails(dish: Dish)

    @Query("SELECT * FROM `dishes-table` ORDER BY ID")
    fun getAllDishesList(): Flow<List<Dish>>
}