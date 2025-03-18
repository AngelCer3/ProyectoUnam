package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuTrabajadorActivity : AppCompatActivity() {

    private lateinit var agregar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_trabajador)

        agregar = findViewById(R.id.btn_agregar)

        agregar.setOnClickListener {
            agregarFormato()
        }
    }
    private fun agregarFormato(){
        intent = Intent(this, Formatoparte1Activity::class.java)
        startActivity(intent)
    }
}