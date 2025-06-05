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

class Formatoparte15Activity : AppCompatActivity() {

    // Declaración de EditTexts
    private lateinit var docCredencialVotarCuenta: EditText
    private lateinit var docCredencialVotarMostro: EditText
    private lateinit var docCredencialVotarEntregoCopia: EditText
    private lateinit var docPoderAmplioCuenta: EditText
    private lateinit var docPoderAmplioMostro: EditText
    private lateinit var docPoderAmplioEntregoCopia: EditText
    private lateinit var docComprobanteIngresosCuenta: EditText
    private lateinit var docComprobanteIngresosMostro: EditText
    private lateinit var docComprobanteIngresosEntregoCopia: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte15)

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")

        // Vincular vistas
        bindViews()

        btnGuardar.setOnClickListener { guardarDatos() }
        btnSiguiente.setOnClickListener { irSiguiente() }
    }

    private fun bindViews() {
        docCredencialVotarCuenta = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialVotarMostro = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialVotarEntregoCopia = findViewById(R.id.doc_credencial_votar_entrego_copia)
        docPoderAmplioCuenta = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderAmplioMostro = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderAmplioEntregoCopia = findViewById(R.id.doc_poder_amplio_entrego_copia)
        docComprobanteIngresosCuenta = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteIngresosMostro = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteIngresosEntregoCopia = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun guardarDatos() {
        if (idAcreditado.isNullOrEmpty()) {
            Toast.makeText(this, "Error: ID de acreditado no disponible", Toast.LENGTH_LONG).show()
            return
        }

        val datos = datosDocumentos(
            doc_credencial_votar_cuenta = docCredencialVotarCuenta.text.toString(),
            doc_credencial_votar_mostro = docCredencialVotarMostro.text.toString(),
            doc_credencial_votar_entrego_copia = docCredencialVotarEntregoCopia.text.toString(),
            doc_poder_amplio_cuenta = docPoderAmplioCuenta.text.toString(),
            doc_poder_amplio_mostro = docPoderAmplioMostro.text.toString(),
            doc_poder_amplio_entrego_copia = docPoderAmplioEntregoCopia.text.toString(),
            doc_comprobante_ingresos_cuenta = docComprobanteIngresosCuenta.text.toString(),
            doc_comprobante_ingresos_mostro = docComprobanteIngresosMostro.text.toString(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteIngresosEntregoCopia.text.toString(),
            id_acreditado = idAcreditado!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosDocumentos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte15Activity,
                            "Datos de documentos guardados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@Formatoparte15Activity,
                            "Error al guardar los datos: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte15Activity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte16Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}