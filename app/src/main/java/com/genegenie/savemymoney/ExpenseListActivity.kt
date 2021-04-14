package com.genegenie.savemymoney

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.genegenie.savemymoney.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ExpenseListActivity : AppCompatActivity() {

    val TAG = "ExpenseListActivity"
    val MENU_ACTION_REMOVE_ID = 1;

    lateinit var db: FirebaseFirestore
    lateinit var listViewData: MutableList<Expense>
    lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)
        db = FirebaseFirestore.getInstance()

        getAllRecords()
    }

    private fun getAllRecords() {
        Log.d(TAG, "getAllRecords()")

        db.collection(Utils.getCurrentMonthCollectionName())
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                this.runOnUiThread {
                    val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                    listViewData = documents.toObjects(Expense::class.java)
                    expenseAdapter = ExpenseAdapter(listViewData, this)
                    recyclerView.adapter = expenseAdapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "getAllRecords()", it)
            }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(
            0, MENU_ACTION_REMOVE_ID,
            findViewById<RecyclerView>(R.id.recycler_view).getChildAdapterPosition(v!!),
            getString(R.string.list_activity_remove_menu_action)
        )
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == MENU_ACTION_REMOVE_ID) {
            val record = listViewData[item.order]
            db.collection(Utils.getCurrentMonthCollectionName()).document(record.id!!)
                .delete()
                .addOnSuccessListener {
                    listViewData.removeAt(item.order)
                    expenseAdapter.notifyItemRemoved(item.order)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "onContextItemSelected(): item=$item", e)
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
        return super.onContextItemSelected(item)
    }
}
