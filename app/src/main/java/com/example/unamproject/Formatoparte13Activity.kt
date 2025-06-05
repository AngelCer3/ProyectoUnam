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

class Formatoparte13Activity : AppCompatActivity() {

    private lateinit var telefono1Lada: EditText
    private lateinit var telefono1Numero: EditText
    private lateinit var telefono1Extension: EditText
    private lateinit var telefono1Tipo: EditText
    private lateinit var telefono2Lada: EditText
    private lateinit var telefono2Numero: EditText
    private lateinit var telefono2Extension: EditText
    private lateinit var telefono2Tipo: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte13)


        idAcreditado = intent.getStringExtra("id_acreditado")


        telefono1Lada = findViewById(R.id.telefono1_lada)
        telefono1Numero = findViewById(R.id.telefono1_numero)
        telefono1Extension = findViewById(R.id.telefono1_extension)
        telefono1Tipo = findViewById(R.id.telefono1_tipo)
        telefono2Lada = findViewById(R.id.telefono2_lada)
        telefono2Numero = findViewById(R.id.telefono2_numero)
        telefono2Extension = findViewById(R.id.telefono2_extension)
        telefono2Tipo = findViewById(R.id.telefono2_tipo)

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

        val datos = datosTelefonos(
            telefono1_lada = telefono1Lada.text.toString(),
            telefono1_numero = telefono1Numero.text.toString(),
            telefono1_extension = telefono1Extension.text.toString(),
            telefono1_tipo = telefono1Tipo.text.toString(),
            telefono2_numero = telefono2Numero.text.toString(),
            telefono2_extension = telefono2Extension.text.toString(),
            telefono2_tipo = telefono2Tipo.text.toString(),
            id_acreditado = idAcreditado!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosTelefonicos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte13Activity, "Datos de teléfonos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte13Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte13Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irSiguiente() {
         val intent = Intent(this, Formatoparte14Activity::class.java)
         intent.putExtra("id_acreditado", idAcreditado)
         startActivity(intent)
    }
}