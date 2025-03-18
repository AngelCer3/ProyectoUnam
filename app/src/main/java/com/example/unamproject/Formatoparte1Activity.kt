package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Formatoparte1Activity : AppCompatActivity() {

    private lateinit var siguiente : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte1)

        siguiente = findViewById(R.id.btnSiguiente)

        siguiente.setOnClickListener {
            siguienteFormato()
        }
    }

    private fun siguienteFormato(){
        intent = Intent(this, Formatoparte2Activity::class.java)
        startActivity(intent)
    }
}