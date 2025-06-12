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
import com.example.unamproject.datosTelefonos
import kotlinx.coroutines.launch

class actualizarFormatoparte13 : AppCompatActivity() {

    private lateinit var telefono1Lada: EditText
    private lateinit var telefono1Numero: EditText
    private lateinit var telefono1Extension: EditText
    private lateinit var telefono1Tipo: EditText
    private lateinit var telefono2Lada: EditText
    private lateinit var telefono2Numero: EditText
    private lateinit var telefono2Extension: EditText
    private lateinit var telefono2Tipo: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte13)


        telefono1Lada = findViewById(R.id.telefono1_lada)
        telefono1Numero = findViewById(R.id.telefono1_numero)
        telefono1Extension = findViewById(R.id.telefono1_extension)
        telefono1Tipo = findViewById(R.id.telefono1_tipo)
        telefono2Lada = findViewById(R.id.telefono2_lada)
        telefono2Numero = findViewById(R.id.telefono2_numero)
        telefono2Extension = findViewById(R.id.telefono2_extension)
        telefono2Tipo = findViewById(R.id.telefono2_tipo)

        btnActualizar = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosTelefonicos()

        btnActualizar.setOnClickListener {
            actualizarDatosTelefonicos()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, actualizarFormatoparte14::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }
    }

    private fun cargarDatosTelefonicos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosTelefonos(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    telefono1Lada.setText(datos.telefono1_lada)
                    telefono1Numero.setText(datos.telefono1_numero)
                    telefono1Extension.setText(datos.telefono1_extension)
                    telefono1Tipo.setText(datos.telefono1_tipo)
                    telefono2Lada.setText(datos.telefono2_lada)
                    telefono2Numero.setText(datos.telefono2_numero)
                    telefono2Extension.setText(datos.telefono2_extension)
                    telefono2Tipo.setText(datos.telefono2_tipo)
                } else {
                    Toast.makeText(this@actualizarFormatoparte13, "No se encontraron datos telefónicos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte13, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosTelefonicos() {
        val datos = datosTelefonos(
            telefono1_lada = telefono1Lada.text.toString(),
            telefono1_numero = telefono1Numero.text.toString(),
            telefono1_extension = telefono1Extension.text.toString(),
            telefono1_tipo = telefono1Tipo.text.toString(),
            telefono2_lada = telefono2Lada.text.toString(),
            telefono2_numero = telefono2Numero.text.toString(),
            telefono2_extension = telefono2Extension.text.toString(),
            telefono2_tipo = telefono2Tipo.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosTelefonicos(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte13, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte13, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte13, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
