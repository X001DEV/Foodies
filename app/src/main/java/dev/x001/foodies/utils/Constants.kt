package dev.x001.foodies.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String ="Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String ="Online"

    val dishTypes = arrayListOf(
        "Breakfast",
        "Lunch",
        "Snacks",
        "Dinner",
        "Salad",
        "Side Dish",
        "Dessert",
        "Other"
    )

    val dishCategory = arrayListOf(
        "Pizza",
        "BBQ",
        "Bakery",
        "Burger",
        "Cafe",
        "Chicken",
        "Dessert",
        "Drinks",
        "Hot Dogs",
        "Juices",
        "Sandwich",
        "Tea & Coffee",
        "Wraps",
        "Other"
    )

    val dishCookTime = arrayListOf(
        "10",
        "15",
        "20",
        "30",
        "45",
        "50",
        "60",
        "90",
        "120",
        "150",
        "180"
    )
}