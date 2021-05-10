package com.genegenie.savemymoney

enum class Category(val id: Int, val resId : Int) {
    FOOD(0, R.string.categories_name_0),
    FUEL(1, R.string.categories_name_1),
    CLOTHES(2, R.string.categories_name_2),
    FAST_FOOD(3, R.string.categories_name_3),
    PHARMACY(4, R.string.categories_name_4),
    GIFTS(5, R.string.categories_name_5),
    ENTERTAINMENT(6, R.string.categories_name_6),
    HOME(7, R.string.categories_name_7),
    CAR(8, R.string.categories_name_8),
    TRAVELS(9, R.string.categories_name_9),
    TRANSPORT(10, R.string.categories_name_10),
    OTHER(11, R.string.categories_name_11),
    BEAUTY_CARE(12, R.string.categories_name_12),
    BIG_PURCHASE(13, R.string.categories_name_13);

    companion object {
        fun getFromId (id : Int?): Category? {
            for (category in Category.values()) {
                if (category.id == id) {
                    return category
                }
            }
            return null
        }
    }
}