package dev.x001.foodies.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.x001.foodies.model.entities.Dish
import kotlinx.coroutines.flow.Flow


@Dao
interface DishDao {

    @Insert
    suspend fun insertDishDetails(dish: Dish)

    @Query("SELECT * FROM `dishes-table` ORDER BY ID")
    fun getAllDishesList(): Flow<List<Dish>>

    @Update
    suspend fun updateDishDetails(dish: Dish)

    @Query("SELECT * FROM `dishes-table` WHERE favorite_dish = 1")
    fun getFavoriteDishesList(): Flow<List<Dish>>

    @Delete
    suspend fun deleteDishDetails(dish: Dish)

    @Query("SELECT * FROM `dishes-table` WHERE type = :filterType")
    fun getFilteredDishesList(filterType: String): Flow<List<Dish>>
}