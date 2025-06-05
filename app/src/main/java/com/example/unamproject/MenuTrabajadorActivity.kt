package com.example.unamproject

import AcreditadosAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.actualizar.actualizarFormatoparte1
import kotlinx.coroutines.launch

class MenuTrabajadorActivity : AppCompatActivity() {

    private lateinit var agregar: Button
    private lateinit var rvAcreditados: RecyclerView
    private lateinit var adapter: AcreditadosAdapter

    private val retrofitService = RetrofitClient.webService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_trabajador)

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
                        val intent = Intent(this@MenuTrabajadorActivity, actualizarFormatoparte1::class.java).apply {
                            putExtra("acreditado", acreditado)
                        }
                        startActivity(intent)
                    }

                rvAcreditados.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MenuTrabajadorActivity, "Error al cargar acreditados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
