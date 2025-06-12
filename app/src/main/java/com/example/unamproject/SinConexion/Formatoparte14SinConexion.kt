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

class Formatoparte14SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var idAcreditado: String

    private lateinit var cobranzaVisitaEt: EditText
    private lateinit var cobranzaNumeroVisitasEt: EditText
    private lateinit var cobranzaUltimaFechaVisitaEt: EditText
    private lateinit var cobranzaDespachoEt: EditText
    private lateinit var cobranzaCalificacionEt: EditText
    private lateinit var cobranzaComentarioEt: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte14_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        cobranzaVisitaEt = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitasEt = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisitaEt = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespachoEt = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacionEt = findViewById(R.id.cobranza_calificacion)
        cobranzaComentarioEt = findViewById(R.id.cobranza_comentario)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatosCobranza()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Formatoparte15SinConexion::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun guardarDatosCobranza() {
        val datos = DatosCobranzaEntity(
            cobranza_visita = cobranzaVisitaEt.text.toString(),
            cobranza_numero_visitas = cobranzaNumeroVisitasEt.text.toString(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisitaEt.text.toString(),
            cobranza_despacho = cobranzaDespachoEt.text.toString(),
            cobranza_calificacion = cobranzaCalificacionEt.text.toString(),
            cobranza_comentario = cobranzaComentarioEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosCobranzaDao().insertDatosCobranza(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte14SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte14SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
