package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte12Activity : AppCompatActivity() {

    private lateinit var familiaTieneDeudas: EditText
    private lateinit var familiaCantidadDeuda: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte12)

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")

        // Vincular vistas con sus IDs correspondientes
        familiaTieneDeudas = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeuda = findViewById(R.id.familia_cantidad_deuda)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener { guardarDatos() }
        btnSiguiente.setOnClickListener { irSiguiente() }
    }

    private fun guardarDatos() {
        if (idAcreditado.isNullOrEmpty()) {
            Toast.makeText(this, "Error: ID de acreditado no disponible", Toast.LENGTH_LONG).show()
            return
        }
        val datos = datosFamiliaDeudas(
            familia_tiene_deudas = familiaTieneDeudas.text.toString(),
            familia_cantidad_deudas = familiaCantidadDeuda.text.toString(),
            id_acreditado = idAcreditado!!
        )

        // Enviar los datos al servidor en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosFamiliaDeudas(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte12Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte12Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte12Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte13Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}