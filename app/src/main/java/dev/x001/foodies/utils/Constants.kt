package dev.x001.foodies.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String ="Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String ="Online"

    const val EXTRA_DISH_DETAILS: String = "DishDetails"

    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"

    const val API_ENDPOINT: String = "recipes/random"

    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"

    const val BASE_URL: String = "https://api.spoonacular.com/"
    const val API_KEY_VALUE: String = "9d69d67d7dbd40afb573b89693d4c6a8"

    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = ""
    const val NUMBER_VALUE: Int = 1

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

    val dishFilters = arrayListOf(
        "All",
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