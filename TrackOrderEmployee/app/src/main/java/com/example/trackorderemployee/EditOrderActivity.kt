package com.example.trackorderemployee

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditOrderActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUserRole: String
    private var courierEmail: String? = null
    private var warehouseEmail: String? = null
    private lateinit var orderId: String

    companion object {
        private const val EXTRA_SEARCH_TEXT = "search_text"

        fun newIntent(context: Context, searchText: String): Intent {
            val intent = Intent(context, EditOrderActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_TEXT, searchText)
            return intent
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getStringExtra(EXTRA_SEARCH_TEXT).toString()
        setContentView(R.layout.activity_edit_order)

        val button3: Button = findViewById(R.id.button3)
        registerForContextMenu(button3)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Пользователь не авторизован, перенаправление на страницу входа
            finish()
        } else {
            // Получение роли текущего пользователя
            val uid = currentUser.uid
            val userDocRef = firestore.collection("users").document(uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        currentUserRole = document.getString("role") ?: ""
                    } else {
                        // Документ пользователя не найден
                        currentUserRole = ""
                    }
                }
                .addOnFailureListener { exception ->
                    // Ошибка при получении документа пользователя
                    currentUserRole = ""
                }
        }

        // Получаем данные о заказе из базы данных и выводим в XML-разметку
        val orderRef = firestore.collection("orders").document(orderId)
        val textViewProductName: TextView = findViewById(R.id.textViewProductName)
        val textViewQuantity: TextView = findViewById(R.id.textViewQuantity)
        val textViewCustomerName: TextView = findViewById(R.id.textViewCustomerName)
        val textViewCustomerAddress: TextView = findViewById(R.id.textViewCustomerAddress)
        val textViewCustomerPhoneNumber: TextView = findViewById(R.id.textViewCustomerPhoneNumber)

        orderRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Получаем данные о заказе из документа
                    val productName = documentSnapshot.getString("productName")
                    val quantity = documentSnapshot.getString("quantity")
                    val customerName = documentSnapshot.getString("customerName")
                    val customerAddress = documentSnapshot.getString("address")
                    val customerPhoneNumber = documentSnapshot.getString("phone")

                    // Устанавливаем значения в TextView
                    textViewProductName.text = "Название товара: $productName"
                    textViewQuantity.text = "Количество: $quantity"
                    textViewCustomerName.text = "ФИО клиента: $customerName"
                    textViewCustomerAddress.text = "Адрес клиента: $customerAddress"
                    textViewCustomerPhoneNumber.text = "Номер телефона клиента: $customerPhoneNumber"
                } else {
                    Toast.makeText(this, "Заказ не найден", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка при получении заказа", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val orderStatus = when (item.itemId) {
            R.id.item_1 -> "Заказ на сборке"
            R.id.item_2 -> "Заказ передан курьеру"
            R.id.item_3 -> "Доставлено"
            else -> return super.onContextItemSelected(item)
        }

        // Получаем текущего пользователя
        val currentUser = firebaseAuth.currentUser

        // Проверяем, что пользователь авторизован
        if (currentUser != null) {
            // Получаем почту текущего пользователя
            val userEmail = currentUser.email

            // Получаем почту курьера и рабочего склада в зависимости от роли пользователя
            if (currentUserRole == "courier") {
                courierEmail = userEmail
            } else if (currentUserRole == "warehouseEmployee") {
                warehouseEmail = userEmail
            }

            // Обновляем статус заказа в базе данных Firebase
            updateOrderStatus(orderId, orderStatus, courierEmail, warehouseEmail)
        } else {
            Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    private fun updateOrderStatus(
        orderId: String,
        status: String,
        courierEmail: String?,
        warehouseEmail: String?
    ) {
        val orderRef = firestore.collection("orders").document(orderId)

        orderRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Обновляем статус заказа в базе данных Firebase
                    val updateData = mutableMapOf<String, Any>()
                    updateData["status"] = status

                    if (!courierEmail.isNullOrEmpty()) {
                        updateData["courierEmail"] = courierEmail!!
                    }

                    if (!warehouseEmail.isNullOrEmpty()) {
                        updateData["warehouseEmail"] = warehouseEmail!!
                    }

                    orderRef.update(updateData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Статус заказа изменен", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Ошибка при изменении статуса заказа", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Заказ не найден", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка при получении заказа", Toast.LENGTH_SHORT).show()
            }
    }
}


