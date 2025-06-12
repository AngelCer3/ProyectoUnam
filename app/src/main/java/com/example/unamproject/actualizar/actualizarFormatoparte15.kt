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
import com.example.unamproject.datosDocumentos
import kotlinx.coroutines.launch

class actualizarFormatoparte15 : AppCompatActivity() {

    private lateinit var docCredencialCuenta: EditText
    private lateinit var docCredencialMostro: EditText
    private lateinit var docCredencialEntrego: EditText
    private lateinit var docPoderCuenta: EditText
    private lateinit var docPoderMostro: EditText
    private lateinit var docPoderEntrego: EditText
    private lateinit var docComprobanteCuenta: EditText
    private lateinit var docComprobanteMostro: EditText
    private lateinit var docComprobanteEntrego: EditText

    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte15)

        // Vincular EditText
        docCredencialCuenta = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialMostro = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialEntrego = findViewById(R.id.doc_credencial_votar_entrego_copia)
        docPoderCuenta = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderMostro = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderEntrego = findViewById(R.id.doc_poder_amplio_entrego_copia)
        docComprobanteCuenta = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteMostro = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteEntrego = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

        btnActualizar = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosDocumentos()

        btnActualizar.setOnClickListener {
            actualizarDatosDocumentos()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, actualizarFormatoparte16::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }
    }

    private fun cargarDatosDocumentos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosDocumentos(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    docCredencialCuenta.setText(datos.doc_credencial_votar_cuenta)
                    docCredencialMostro.setText(datos.doc_credencial_votar_mostro)
                    docCredencialEntrego.setText(datos.doc_credencial_votar_entrego_copia)
                    docPoderCuenta.setText(datos.doc_poder_amplio_cuenta)
                    docPoderMostro.setText(datos.doc_poder_amplio_mostro)
                    docPoderEntrego.setText(datos.doc_poder_amplio_entrego_copia)
                    docComprobanteCuenta.setText(datos.doc_comprobante_ingresos_cuenta)
                    docComprobanteMostro.setText(datos.doc_comprobante_ingresos_mostro)
                    docComprobanteEntrego.setText(datos.doc_comprobante_ingresos_entrego_copia)
                } else {
                    Toast.makeText(this@actualizarFormatoparte15, "No se encontraron datos del acreditado", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte15, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosDocumentos() {
        val datos = datosDocumentos(
            doc_credencial_votar_cuenta = docCredencialCuenta.text.toString(),
            doc_credencial_votar_mostro = docCredencialMostro.text.toString(),
            doc_credencial_votar_entrego_copia = docCredencialEntrego.text.toString(),
            doc_poder_amplio_cuenta = docPoderCuenta.text.toString(),
            doc_poder_amplio_mostro = docPoderMostro.text.toString(),
            doc_poder_amplio_entrego_copia = docPoderEntrego.text.toString(),
            doc_comprobante_ingresos_cuenta = docComprobanteCuenta.text.toString(),
            doc_comprobante_ingresos_mostro = docComprobanteMostro.text.toString(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteEntrego.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosDocumentos(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte15, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte15, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte15, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
