package com.example.unamproject

import TrabajadorAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class TrabajadoresActivity : AppCompatActivity() {

    private lateinit var btnAgregar: Button
    private lateinit var recyclerTrabajadores: RecyclerView
    private lateinit var adapter: TrabajadorAdapter

    private val retrofitService = RetrofitClient.webService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trabajadores)

        btnAgregar = findViewById(R.id.btn_agregar_trabajador)
        recyclerTrabajadores = findViewById(R.id.recyclerTrabajadores)

        recyclerTrabajadores.layoutManager = LinearLayoutManager(this)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, RegistrarTrabajadorActivity::class.java)
            startActivity(intent)
        }

        cargarTrabajadores()
    }

    private fun cargarTrabajadores() {

        lifecycleScope.launch {

            try {

                val trabajadores = retrofitService.obtenerTrabajadores()

                adapter = TrabajadorAdapter(trabajadores)

                recyclerTrabajadores.adapter = adapter

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@TrabajadoresActivity,
                    "Error al cargar trabajadores",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }
}