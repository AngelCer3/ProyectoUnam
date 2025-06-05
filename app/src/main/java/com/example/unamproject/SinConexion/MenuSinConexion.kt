package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.SinConexion.Formatoparte1ActivitySinConexion
import com.example.unamproject.R

class MenuSinConexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_sin_conexion)

        val btnAgregar = findViewById<Button>(R.id.btn_agregar)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, Formatoparte1ActivitySinConexion::class.java)
            startActivity(intent)
        }
    }
}
