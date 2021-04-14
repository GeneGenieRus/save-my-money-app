package com.genegenie.savemymoney

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val dateFormat = SimpleDateFormat("yyyy-MM")

    fun  getCurrentMonthCollectionName(): String {
        return dateFormat.format(Date())
    }
}