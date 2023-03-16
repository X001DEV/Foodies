package dev.x001.foodies.viewmodel

import androidx.lifecycle.*
import dev.x001.foodies.model.database.DishRepository
import dev.x001.foodies.model.entities.Dish
import kotlinx.coroutines.launch

class DishViewModel(private val repository: DishRepository): ViewModel() {

    fun insert(dish: Dish) = viewModelScope.launch {
        repository.insertDishData(dish)
    }

    val allDishesList: LiveData<List<Dish>> = repository.allDishesList.asLiveData()

    fun update(dish: Dish) = viewModelScope.launch {
        repository.updateDishData(dish)
    }

    val favoriteDishes: LiveData<List<Dish>> = repository.favoriteDishes.asLiveData()

    fun delete(dish: Dish) = viewModelScope.launch {
        repository.deleteDishData(dish)
    }

}

class DishViewModelFactory(private val repository: DishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}