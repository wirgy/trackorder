package com.example.trackorders

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class TrackOrderActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_SEARCH_TEXT = "search_text"
        private const val EXTRA_STATUS = "status"

        fun newIntent(context: Context, searchText: String, status: String): Intent {
            val intent = Intent(context, TrackOrderActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_TEXT, searchText)
            intent.putExtra(EXTRA_STATUS, status)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)

        val searchText = intent.getStringExtra(EXTRA_SEARCH_TEXT)
        val status = intent.getStringExtra(EXTRA_STATUS)

        val textOrder = findViewById<TextView>(R.id.textOrder)

        textOrder.text = searchText

        // Отображение интерфейса на основе статуса
        val textOrderPlaced = findViewById<TextView>(R.id.textOrderPlaced)
        val viewOrderPlaced = findViewById<View>(R.id.viewOrderPlaced)

        val textOrderConfirmed = findViewById<TextView>(R.id.textOrderConfirmed)
        val viewOrderConfirmed = findViewById<View>(R.id.viewOrderConfirmed)
        val view3 = findViewById<View>(R.id.view3)

        val textOrderProcessed = findViewById<TextView>(R.id.textOrderProcessed)
        val viewOrderProcessed = findViewById<View>(R.id.viewOrderProcessed)
        val view4 = findViewById<View>(R.id.view4)

        if (status == "") {
            textOrderPlaced.visibility = View.GONE
            viewOrderPlaced.visibility = View.GONE

            textOrderConfirmed.visibility = View.GONE
            viewOrderConfirmed.visibility = View.GONE
            view3.visibility = View.GONE

            textOrderProcessed.visibility = View.GONE
            viewOrderProcessed.visibility = View.GONE
            view4.visibility = View.GONE
        } else if (status == "Заказ на сборке") {
            textOrderPlaced.visibility = View.VISIBLE
            viewOrderPlaced.visibility = View.VISIBLE

            textOrderConfirmed.visibility = View.GONE
            viewOrderConfirmed.visibility = View.GONE
            view3.visibility = View.GONE

            textOrderProcessed.visibility = View.GONE
            viewOrderProcessed.visibility = View.GONE
            view4.visibility = View.GONE
        } else if (status == "Заказ передан курьеру") {
            textOrderPlaced.visibility = View.VISIBLE
            viewOrderPlaced.visibility = View.VISIBLE

            textOrderConfirmed.visibility = View.VISIBLE
            viewOrderConfirmed.visibility = View.VISIBLE
            view3.visibility = View.VISIBLE

            textOrderProcessed.visibility = View.GONE
            viewOrderProcessed.visibility = View.GONE
            view4.visibility = View.GONE
        } else if (status == "Доставлено") {
            textOrderPlaced.visibility = View.VISIBLE
            viewOrderPlaced.visibility = View.VISIBLE

            textOrderConfirmed.visibility = View.VISIBLE
            viewOrderConfirmed.visibility = View.VISIBLE
            view3.visibility = View.VISIBLE

            textOrderProcessed.visibility = View.VISIBLE
            viewOrderProcessed.visibility = View.VISIBLE
            view4.visibility = View.VISIBLE
        }
    }
}
