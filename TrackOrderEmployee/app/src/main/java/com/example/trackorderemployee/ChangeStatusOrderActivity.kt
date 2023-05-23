package com.example.trackorderemployee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

class ChangeStatusOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_status_order)
        val button3: Button = findViewById(R.id.button3)
        registerForContextMenu(button3)
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

        when(item.itemId){
            R.id.item_1 -> Toast.makeText(this, "статус заказа изменен", Toast.LENGTH_SHORT).show()
            R.id.item_2 -> Toast.makeText(this, "статус заказа изменен", Toast.LENGTH_SHORT).show()
            R.id.item_3 -> Toast.makeText(this, "статус заказа изменен", Toast.LENGTH_SHORT).show()
            R.id.item_4 -> Toast.makeText(this, "статус заказа изменен", Toast.LENGTH_SHORT).show()
        }

        return super.onContextItemSelected(item)
    }

}