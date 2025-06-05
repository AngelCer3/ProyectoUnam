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
    private lateinit var adapter: AcreditadosAdapter

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

        cargarAcreditados()
    }

    private fun agregarFormato() {
        val intent = Intent(this, Formatoparte1Activity::class.java)
        startActivity(intent)
    }

    private fun cargarAcreditados() {
        lifecycleScope.launch {
            try {
                val listaAcreditados = retrofitService.ObtenerAcreditados()

                adapter = AcreditadosAdapter(listaAcreditados) { acreditado ->
                    val intent = Intent(this@MenuAdministradorActivity, actualizarFormatoparte1::class.java).apply {
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
}