/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.genegenie.savemymoney

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.genegenie.savemymoney.model.Expense
import java.text.SimpleDateFormat

class ExpenseAdapter(val list: List<Expense>, val context : AppCompatActivity) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View, private val context: AppCompatActivity) :
        RecyclerView.ViewHolder(view) {
        private val amountView: TextView = itemView.findViewById(R.id.record_item_amount)
        private val categoryView: TextView = itemView.findViewById(R.id.record_item_category)
        private val descriptionView: TextView = itemView.findViewById(R.id.record_item_description)
        private val accountView: TextView = itemView.findViewById(R.id.record_item_account)
        private val dateView: TextView = itemView.findViewById(R.id.record_item_date)
        private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

        init {
            view.setOnCreateContextMenuListener(context)
        }

        fun bind(expense: Expense) {
            amountView.text = expense.amount?.toString()?:""
            val resId = Category.getFromId(expense.category)?.resId
            categoryView.text = if (resId != null) context.getString(resId) else ""
            descriptionView.text = expense.description?:""
            accountView.text = expense.account?:""
            dateView.text = if (expense.date != null) simpleDateFormat.format(expense.date) else ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.record_item, parent, false)

        return ExpenseViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(list[position])
    }
}