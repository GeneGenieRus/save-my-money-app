package com.genegenie.savemymoney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.genegenie.savemymoney.Constants.PRICE_INPUT_REQUEST_CODE
import com.genegenie.savemymoney.Constants.PRICE_INPUT_RESULT_CODE
import com.google.android.material.textfield.TextInputEditText

class PriceInputActivity : AppCompatActivity() {

    private val TAG = "PriceInputActivity"

    var amount : String = ""
    var amountView : TextView? = null

    private val buttons : Array<Int> = arrayOf(R.id.buttonPrice0Id, R.id.buttonPrice00Id, R.id.buttonPrice1Id, R.id.buttonPrice2Id,
        R.id.buttonPrice3Id,R.id.buttonPrice4Id, R.id.buttonPrice5Id, R.id.buttonPrice6Id, R.id.buttonPrice7Id,
        R.id.buttonPrice8Id, R.id.buttonPrice9Id)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        amountView = findViewById(R.id.priceInputView)

        //number buttons
        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener {
                amount += (it as Button).text
                amountView?.text = amount
            }
        }

        //next button
        findViewById<Button>(R.id.buttonPriceNextId).setOnClickListener {
            if (amount.isEmpty()) {
                return@setOnClickListener
            }
            val newIntent = Intent()
            newIntent.putExtra("amount", amount.toInt())
            newIntent.putExtra("description", findViewById<TextInputEditText>(R.id.descriptionInputId).text?.toString())
            setResult(PRICE_INPUT_RESULT_CODE, newIntent)
            finish()
        }

        //remove button
        findViewById<Button>(R.id.buttonPriceRemoveId).setOnClickListener {
            amount = amount.dropLast(1)
            amountView?.text = amount
        }
    }


}