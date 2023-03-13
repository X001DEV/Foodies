package dev.x001.foodies.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes-table")
data class Dish(
    val image: String,
    val imageSource: String,
    val dish: String,
    val type: String,
    val category: String,
    val ingredients: String,
    @ColumnInfo(name ="cooking_time")
    val cookingTime: String,
    @ColumnInfo(name = "instructions")
    val directionToCook: String,
    @ColumnInfo(name = "favorite_dish")
    var favoriteDish: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)