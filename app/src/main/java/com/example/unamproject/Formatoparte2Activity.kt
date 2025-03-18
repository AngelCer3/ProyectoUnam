package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Formatoparte2Activity : AppCompatActivity() {

    private lateinit var regresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte2)

        regresar = findViewById(R.id.btnGuardarNuevaSeccion)

        regresar.setOnClickListener {
            regresarFormato()
        }
    }

    private fun regresarFormato(){
        intent = Intent(this, Formatoparte1Activity::class.java)
        startActivity(intent)
    }
}