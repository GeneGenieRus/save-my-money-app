package com.genegenie.savemymoney.model

import com.genegenie.savemymoney.Category
import java.util.*

class MonthTotal(val total: Int, val categories: List<MonthTotalRow>, val currentDate : Date) {

    override fun toString(): String {
        return "MonthTotal(total=$total, categories=$categories, currentDate=$currentDate)"
    }
}

class MonthTotalRow(val category: Category, val amount: Int) {
    override fun toString(): String {
        return "MonthTotalRow(category=$category, amount=$amount)"
    }
}
