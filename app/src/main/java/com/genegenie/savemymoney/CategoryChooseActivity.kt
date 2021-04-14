package com.genegenie.savemymoney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.genegenie.savemymoney.Constants.CATEGORY_CHOOSE_RESULT_CODE
import com.genegenie.savemymoney.Constants.PRICE_INPUT_REQUEST_CODE

class CategoryChooseActivity : AppCompatActivity() {

    private val TAG = "CategoryChooseActivity"

    var selected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        addButtons()
    }

    private fun addButtons() {
        val layout = findViewById<LinearLayout>(R.id.categoriesLinearLayoutId)

        for (category in Category.values()) {
            val btn = Button(this)
            btn.text = getString(category.resId)
            btn.setOnClickListener {
                selected = category.id
                val intent = Intent(this, PriceInputActivity::class.java)
                intent.putExtra("category", category)
                startActivityForResult(Intent(this, PriceInputActivity::class.java), PRICE_INPUT_REQUEST_CODE)
            }
            layout.addView(btn)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PRICE_INPUT_REQUEST_CODE && resultCode == Constants.PRICE_INPUT_RESULT_CODE) {
            intent.putExtra("amount", data?.extras?.get("amount") as Int?)
            intent.putExtra("description", data?.extras?.get("description") as String?)
            intent.putExtra("category", selected)
            setResult(CATEGORY_CHOOSE_RESULT_CODE, intent)
            finish()
        }
    }
}