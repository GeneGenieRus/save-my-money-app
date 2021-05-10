package com.genegenie.savemymoney

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genegenie.savemymoney.Constants.DB_COLLECTION_MONTHS_CATEGORY_PREFIX
import com.genegenie.savemymoney.Constants.DB_COLLECTION_MONTHS_TOTAL
import com.genegenie.savemymoney.model.MonthTotal
import com.genegenie.savemymoney.model.MonthTotalRow
import com.genegenie.savemymoney.model.ResponseWrapper
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.stream.Collectors

class MonthTotalViewModel : ViewModel() {

    private val TAG = "MonthTotalViewModel"

    var selectedDate: Date = Date()

    val monthTotal: MutableLiveData<ResponseWrapper<MonthTotal>> by lazy {
        MutableLiveData<ResponseWrapper<MonthTotal>>().also {
            loadTotal()
        }
    }


    private fun loadTotal() {
        FirebaseFirestore.getInstance()
            .collection(Constants.DB_COLLECTION_MONTHS)
            .document(Utils.getCollectionNameFromDate(selectedDate))
            .get()
            .addOnSuccessListener { document ->
                val mutableData = document.data ?: Collections.EMPTY_MAP
                val totalInt = (mutableData.remove(DB_COLLECTION_MONTHS_TOTAL) as? Number)?.toInt() ?: 0
                val categoriesRows = mutableData.entries.stream()
                    .filter { x -> x.key.toString().startsWith(DB_COLLECTION_MONTHS_CATEGORY_PREFIX) }
                    .map { x ->
                        val categoryId = x.key.toString().replace(DB_COLLECTION_MONTHS_CATEGORY_PREFIX, "").toIntOrNull()
                        MonthTotalRow(
                            Category.getFromId(categoryId) ?: Category.OTHER,
                            (x.value as Number).toInt()
                        )
                    }
                    .sorted(Comparator.comparingInt(MonthTotalRow::amount).reversed())
                    .collect(Collectors.toList())

                monthTotal.value = ResponseWrapper.Success(MonthTotal(totalInt, categoriesRows, selectedDate))
            }
            .addOnFailureListener {
                Log.e(TAG, "getAllRecords()", it)
                monthTotal.value = ResponseWrapper.Error("Error: ${it.localizedMessage}")
            }
    }

    fun switchMonth(value: Int) {
        val instance = Calendar.getInstance()
        instance.time = selectedDate
        instance.set(Calendar.DAY_OF_MONTH, 1)
        instance.add(Calendar.MONTH, value)
        selectedDate = instance.time

        monthTotal.value = ResponseWrapper.Loading()
        loadTotal()
    }
}