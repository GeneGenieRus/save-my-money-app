package com.genegenie.savemymoney.model

import com.google.firebase.firestore.DocumentId
import java.util.*

class Expense {

    @DocumentId
    var id: String? = null

    var date: Date? = null
    var category: Int? = null
    var description: String? = null
    var amount: Int? = null
    var account: String? = null
    var dateString : String? = null

    override fun toString(): String {
        return "Record(id=$id, date=$date, description=$description, amount=$amount)"
    }
}