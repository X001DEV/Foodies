package dev.x001.foodies.model.database

import androidx.annotation.WorkerThread
import dev.x001.foodies.model.entities.Dish

class DishRepository(private val dishDao: DishDao) {

    @WorkerThread
    suspend fun insertDishData(dish: Dish){
        dishDao.insertDishDetails(dish)
    }

}