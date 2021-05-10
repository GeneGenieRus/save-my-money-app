package com.genegenie.savemymoney

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val dateFormat = SimpleDateFormat("yyyy-MM")

    fun  getCurrentMonthCollectionName(): String {
        return dateFormat.format(Date())
    }

    fun getCollectionNameFromDate(date : Date): String {
        return dateFormat.format(date)
    }

    fun getMonthStringId(month: Int): Int {
        return when (month) {
            0 -> R.string.month_0
            1 -> R.string.month_1
            2 -> R.string.month_2
            3 -> R.string.month_3
            4 -> R.string.month_4
            5 -> R.string.month_5
            6 -> R.string.month_6
            7 -> R.string.month_7
            8 -> R.string.month_8
            9 -> R.string.month_9
            10 -> R.string.month_10
            else -> R.string.month_11
        }
    }
}