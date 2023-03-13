package dev.x001.foodies.model.entities

import androidx.room.Entity

@Entity(tableName = "dishes-table")
data class Dish(
    val image: String,
    val imageSource: String,
    val title: String,
    val type: String,
    val category: String,
    val ingredients: String
)