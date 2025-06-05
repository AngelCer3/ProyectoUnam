package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosFamiliaDeudas
import kotlinx.coroutines.launch

class actualizarFormatoparte12 : AppCompatActivity() {

    private lateinit var familiaTieneDeudas: EditText
    private lateinit var familiaCantidadDeuda: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte12)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vincular EditText con IDs del layout
        familiaTieneDeudas = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeuda = findViewById(R.id.familia_cantidad_deuda)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosDeudasFamiliares()

        btnActualizarDatos.setOnClickListener {
            actualizarDatosDeudasFamiliares()
        }

        btnSiguiente.setOnClickListener {
            irASiguiente()
        }
    }

    private fun cargarDatosDeudasFamiliares() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosFamiliaDeudas(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    familiaTieneDeudas.setText(datos.familia_tiene_deudas)
                    familiaCantidadDeuda.setText(datos.familia_cantidad_deudas)
                } else {
                    Toast.makeText(this@actualizarFormatoparte12, "No se encontraron datos de deudas familiares", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte12, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosDeudasFamiliares() {
        val datos = datosFamiliaDeudas(
            familia_tiene_deudas = familiaTieneDeudas.text.toString(),
            familia_cantidad_deudas = familiaCantidadDeuda.text.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosFamiliaDeudas(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte12, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte12, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte12, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        // Cambiar actualizarFormatoparte13 por la siguiente actividad que corresponda
        val intent = Intent(this, actualizarFormatoparte13::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
