package com.genegenie.savemymoney

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.genegenie.savemymoney.Constants.CATEGORY_CHOOSE_REQUEST_CODE
import com.genegenie.savemymoney.Constants.CATEGORY_CHOOSE_RESULT_CODE
import com.genegenie.savemymoney.Constants.DB_COLLECTION_EXPENSES
import com.genegenie.savemymoney.Constants.DB_COLLECTION_MONTHS
import com.genegenie.savemymoney.Constants.SIGN_IN_REQUEST_CODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = Firebase.auth

        updateTextView()

        findViewById<Button>(R.id.signInButtonId).setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            signIn()
        }

        findViewById<Button>(R.id.signOutButtonId).setOnClickListener {
            Firebase.auth.signOut()
            updateTextView()
        }

        findViewById<Button>(R.id.createRecordButtonId).setOnClickListener {
            startActivityForResult(
                Intent(this, CategoryChooseActivity::class.java), CATEGORY_CHOOSE_REQUEST_CODE
            )
        }

        findViewById<Button>(R.id.openListButtonId).setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }

    }

    private fun updateTextView() {
        Log.d(TAG, "updateTextView():")
        findViewById<TextView>(R.id.textview_first).text =
            mAuth.currentUser?.email ?: getString(R.string.not_logged_msg)
    }

    private fun signIn() {
        Log.d(TAG, "signIn():")
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            authWithFirebase(data)
        }

        if (requestCode == CATEGORY_CHOOSE_REQUEST_CODE && resultCode == CATEGORY_CHOOSE_RESULT_CODE) {
            createExpense(data)
        }
    }

    private fun authWithFirebase(data: Intent?) {
        Log.d(TAG, "authWithFirebase(): data=$data")

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            authWithGoogle(account.idToken!!)
            Toast.makeText(this, getString(R.string.logged_msg), Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createExpense(data: Intent?) {

        val category = data?.extras?.get("category") as Int?
        val amount = data?.extras?.get("amount") as Int?
        val expenseValuesMap = hashMapOf(
            "category" to category,
            "description" to data?.extras?.get("description") as String?,
            "amount" to amount,
            "date" to Date(),
            "account" to mAuth.currentUser?.email
        )

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val monthRef = db.collection(DB_COLLECTION_MONTHS).document(Utils.getCurrentMonthCollectionName())
        val expenseRef = db.collection(DB_COLLECTION_MONTHS).document(Utils.getCurrentMonthCollectionName())
            .collection(DB_COLLECTION_EXPENSES).document()

        db.runTransaction {
            val monthMap = it.get(monthRef).data?:HashMap()
            monthMap.merge("total", amount?:0) { a , b -> (a as Number).toInt() + (b as Number).toInt() }
            monthMap.merge("category_${category}", amount?:0) { a , b -> (a as Number).toInt() + (b as Number).toInt() }
            it.set(monthRef, monthMap, SetOptions.merge())
            it.set(expenseRef, expenseValuesMap)
        }
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.create_document_success_msg),
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e ->
                Log.w(TAG, "Error creating document", e)
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun authWithGoogle(idToken: String) {
        Log.d(TAG, "authWithGoogle(): idToken=$idToken")

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateTextView()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    updateTextView()
                }
            }
    }
}
