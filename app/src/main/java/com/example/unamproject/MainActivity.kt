package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var accederButton : Button
    private lateinit var adminButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accederButton = findViewById(R.id.btn_acceder)
        adminButton = findViewById(R.id.btn_admin)

        accederButton.setOnClickListener {
            accederMenu()
        }
        adminButton.setOnClickListener {
            accederAdmin()
        }

    }

    private fun accederMenu(){
        intent = Intent(this,MenuTrabajadorActivity::class.java)
        startActivity(intent)
    }

    private fun accederAdmin(){
        val intent = Intent(this, AdministradorActivity::class.java)
        startActivity(intent)
    }
}