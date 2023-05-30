package com.example.trackorders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var inputSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputSearch = findViewById(R.id.inputSearch)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val searchText = inputSearch.text.toString()
            val firestore = FirebaseFirestore.getInstance()
            val orderRef = firestore.collection("orders").document(searchText)

            orderRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val status = documentSnapshot.getString("status").toString()
                    val intent = TrackOrderActivity.newIntent(this, searchText, status)
                    startActivity(intent)
                } else {

                    Toast.makeText(this, "Заказ не найден", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->

            }
        }
    }
}


