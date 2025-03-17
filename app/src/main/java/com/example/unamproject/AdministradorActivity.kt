package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdministradorActivity : AppCompatActivity() {

    private lateinit var accederButton : Button
    private lateinit var regresar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        accederButton = findViewById(R.id.btn_acceder)
        regresar = findViewById(R.id.btn_regresar)

        accederButton.setOnClickListener {
            accederAdmin()
        }

        regresar.setOnClickListener {
            regresarVista()
        }

    }

    private fun accederAdmin(){
        intent = Intent(this,MenuAdministradorActivity::class.java)
        startActivity(intent)
    }

    private fun regresarVista(){
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}