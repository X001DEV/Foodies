package dev.x001.foodies.model.database

import androidx.annotation.WorkerThread
import dev.x001.foodies.model.entities.Dish
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao: DishDao) {

    @WorkerThread
    suspend fun insertDishData(dish: Dish){
        dishDao.insertDishDetails(dish)
    }

    val allDishesList: Flow<List<Dish>> = dishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateDishData(dish: Dish){
        dishDao.updateDishDetails(dish)
    }

    val favoriteDishes: Flow<List<Dish>> = dishDao.getFavoriteDishesList()

    @WorkerThread
    suspend fun deleteDishData(dish: Dish){
        dishDao.deleteDishDetails(dish)
    }
}