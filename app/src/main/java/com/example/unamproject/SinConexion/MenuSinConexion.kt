package com.example.unamproject.SinConexion

import AdaptadorAcreditadoSinConexion
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.R
import kotlinx.coroutines.launch

class MenuSinConexion : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAgregar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_sin_conexion)

        btnAgregar = findViewById(R.id.btn_agregar)
        recyclerView = findViewById(R.id.recyclerAcreditadosTrabajadorSinConexion)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, Formatoparte1ActivitySinConexion::class.java)
            startActivity(intent)
        }

        cargarAcreditadosLocales()
    }

    private fun cargarAcreditadosLocales() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val acreditados = db.acreditadoDao().getAllAcreditados()
            recyclerView.adapter = AdaptadorAcreditadoSinConexion(acreditados)
        }
    }
}
