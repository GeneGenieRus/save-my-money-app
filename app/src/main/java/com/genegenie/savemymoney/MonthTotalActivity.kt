package com.genegenie.savemymoney

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.genegenie.savemymoney.model.MonthTotal
import com.genegenie.savemymoney.model.ResponseType
import com.genegenie.savemymoney.model.ResponseWrapper
import java.util.*

class MonthTotalActivity : AppCompatActivity() {

    private val TAG = "MonthTotalActivity"

    private val INT_PREVIOUS = -1
    private val INT_NEXT = 1
    private val INT_PROGRESS_BAR_MAX_VALUE = 110000
    private val INT_PROGRESS_SECONDARY_VALUE = 75000

    private val model: MonthTotalViewModel by viewModels()

    private lateinit var categoriesLayout: LinearLayout
    private lateinit var monthNameTextView: TextView
    private lateinit var monthTotalTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_total)

        bindViews()

        initMonthTotalObserver()

        findViewById<Button>(R.id.buttonLeft).setOnClickListener {
            model.switchMonth(INT_PREVIOUS)
        }
        findViewById<Button>(R.id.buttonRight).setOnClickListener {
            model.switchMonth(INT_NEXT)
        }
    }

    private fun bindViews() {
        categoriesLayout = findViewById(R.id.monthTotalLinearLayout)
        monthNameTextView = findViewById(R.id.monthNameTextView)
        monthTotalTextView = findViewById(R.id.monthTotalTextView)
        loadingIndicator = findViewById(R.id.monthTotalProgressBarLoadingIndicator)
        progressBar = findViewById(R.id.monthTotalProgressBar)
        progressBar.max = INT_PROGRESS_BAR_MAX_VALUE
        progressBar.secondaryProgress = INT_PROGRESS_SECONDARY_VALUE
    }

    private fun initMonthTotalObserver() {
        val monthTotalObserver = Observer<ResponseWrapper<MonthTotal>> { response ->
            when (response.type){
                ResponseType.SUCCESS -> {
                    val monthTotal = response.data!!

                    monthNameTextView.text = getMonthNameString(monthTotal.currentDate)
                    monthTotalTextView.text = monthTotal.total.toString()
                    categoriesLayout.removeAllViews()
                    monthTotal.categories.forEach(){
                        addCategoryTextView(getString(it.category.resId), it.amount)
                    }
                    progressBar.setProgress(monthTotal.total, true)
                    loadingIndicator.visibility = View.GONE
                }
                ResponseType.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                }
                ResponseType.ERROR -> {
                    loadingIndicator.visibility = View.GONE
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        model.monthTotal.observe(this, monthTotalObserver)
    }

    private fun getMonthNameString(date: Date): String {
        val instance = Calendar.getInstance()
        instance.time = date
        val year = instance.get(Calendar.YEAR)
        val month = instance.get(Calendar.MONTH)
        return getString(Utils.getMonthStringId(month)) + " " + year
    }

    private fun addCategoryTextView(name: String, value: Int) {
        val textView = TextView(this)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.text = getString(R.string.categoryTextTemplate , name, value)
        categoriesLayout.addView(textView)
    }
}