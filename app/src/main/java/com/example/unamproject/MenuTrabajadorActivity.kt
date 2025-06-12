package com.example.unamproject

import AcreditadosTrabajadorAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MenuTrabajadorActivity : AppCompatActivity() {

    private lateinit var agregar: Button
    private lateinit var cerrarSesion: Button
    private lateinit var recyclerAcreditadosTrabajador: RecyclerView
    private lateinit var adapter: AcreditadosTrabajadorAdapter
    private var idUsuario: String? = null

    private val retrofitService = RetrofitClient.webService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_trabajador)

        idUsuario = intent.getStringExtra("id_usuario")

        agregar = findViewById(R.id.btn_agregar)
        recyclerAcreditadosTrabajador = findViewById(R.id.recyclerAcreditadosTrabajador)
        recyclerAcreditadosTrabajador.layoutManager = LinearLayoutManager(this)


        cerrarSesion = findViewById(R.id.btn_cerrar_sesion)

        cerrarSesion.setOnClickListener {
            cerrarSesion()
        }
        agregar.setOnClickListener {
            agregarFormato()
        }

        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            // Aquí podrías cerrar la actividad o redirigir al login
            finish()
            return
        }

        cargarAcreditados()
    }

    private fun agregarFormato() {
        val intent = Intent(this, Formatoparte1Activity::class.java)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }

    private fun cargarAcreditados() {
        lifecycleScope.launch {
            try {
                val listaAcreditados = retrofitService.ObtenerAcreditadoPorId(idUsuario!!)

                if (listaAcreditados.isEmpty()) {
                    Toast.makeText(this@MenuTrabajadorActivity, "No hay acreditados para mostrar", Toast.LENGTH_SHORT).show()
                } else {
                    adapter = AcreditadosTrabajadorAdapter(listaAcreditados)
                    recyclerAcreditadosTrabajador.adapter = adapter
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MenuTrabajadorActivity, "Error al cargar acreditados", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cerrarSesion() {
        // Limpiar SharedPreferences (ejemplo)
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Opcional: mostrar mensaje
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        // Ir a pantalla de login y cerrar esta actividad
        val intent = Intent(this, MainActivity::class.java)
        // Para evitar que pueda volver con el botón atrás:
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
