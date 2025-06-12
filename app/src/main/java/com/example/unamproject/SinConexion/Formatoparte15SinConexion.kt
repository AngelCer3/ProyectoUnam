package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte15SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var idAcreditado: String

    private lateinit var docCredencialVotarCuentaEt: EditText
    private lateinit var docCredencialVotarMostroEt: EditText
    private lateinit var docCredencialVotarEntregoCopiaEt: EditText

    private lateinit var docPoderAmplioCuentaEt: EditText
    private lateinit var docPoderAmplioMostroEt: EditText
    private lateinit var docPoderAmplioEntregoCopiaEt: EditText

    private lateinit var docComprobanteIngresosCuentaEt: EditText
    private lateinit var docComprobanteIngresosMostroEt: EditText
    private lateinit var docComprobanteIngresosEntregoCopiaEt: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte15_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        docCredencialVotarCuentaEt = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialVotarMostroEt = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialVotarEntregoCopiaEt = findViewById(R.id.doc_credencial_votar_entrego_copia)

        docPoderAmplioCuentaEt = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderAmplioMostroEt = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderAmplioEntregoCopiaEt = findViewById(R.id.doc_poder_amplio_entrego_copia)

        docComprobanteIngresosCuentaEt = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteIngresosMostroEt = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteIngresosEntregoCopiaEt = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatosDocumentos()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Formatoparte16SinConexion::class.java) // Cambia a la siguiente pantalla
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun guardarDatosDocumentos() {
        val datos = DatosDocumentosEntity(
            doc_credencial_votar_cuenta = docCredencialVotarCuentaEt.text.toString(),
            doc_credencial_votar_mostro = docCredencialVotarMostroEt.text.toString(),
            doc_credencial_votar_entrego_copia = docCredencialVotarEntregoCopiaEt.text.toString(),

            doc_poder_amplio_cuenta = docPoderAmplioCuentaEt.text.toString(),
            doc_poder_amplio_mostro = docPoderAmplioMostroEt.text.toString(),
            doc_poder_amplio_entrego_copia = docPoderAmplioEntregoCopiaEt.text.toString(),

            doc_comprobante_ingresos_cuenta = docComprobanteIngresosCuentaEt.text.toString(),
            doc_comprobante_ingresos_mostro = docComprobanteIngresosMostroEt.text.toString(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteIngresosEntregoCopiaEt.text.toString(),

            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosDocumentosDao().insertDatosDocumentos(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte15SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte15SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
