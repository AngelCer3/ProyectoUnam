package com.example.unamproject

import AcreditadosAdapter
import android.annotation.SuppressLint
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
import com.example.unamproject.actualizar.actualizarFormatoparte1
import kotlinx.coroutines.launch

class MenuAdministradorActivity : AppCompatActivity() {

    private lateinit var agregar: Button
    private lateinit var rvAcreditados: RecyclerView
    private lateinit var cerrarSesion: Button
    private lateinit var adapter: AcreditadosAdapter
    private var idUsuario: String? = null

    private val retrofitService = RetrofitClient.webService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_administrador)
        agregar = findViewById(R.id.btn_agregar)
        rvAcreditados = findViewById(R.id.recyclerAcreditados)
        rvAcreditados.layoutManager = LinearLayoutManager(this)

        agregar.setOnClickListener {
            agregarFormato()
        }
        cerrarSesion = findViewById(R.id.btn_cerrar_sesion)

        cerrarSesion.setOnClickListener {
            cerrarSesion()
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
                val listaAcreditados = retrofitService.ObtenerAcreditados()

                adapter = AcreditadosAdapter(listaAcreditados) { acreditado ->
                    val intent = Intent(this@MenuAdministradorActivity, actualizarFormatoparte1::class.java).apply {
                        intent.putExtra("id_usuario", idUsuario)
                        putExtra("acreditado", acreditado)
                    }
                    startActivity(intent)
                }

                rvAcreditados.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MenuAdministradorActivity, "Error al cargar acreditados", Toast.LENGTH_SHORT).show()
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