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
import com.example.unamproject.datosCobranza
import kotlinx.coroutines.launch

class actualizarFormatoparte14 : AppCompatActivity() {

    private lateinit var cobranzaVisita: EditText
    private lateinit var cobranzaNumeroVisitas: EditText
    private lateinit var cobranzaUltimaFechaVisita: EditText
    private lateinit var cobranzaDespacho: EditText
    private lateinit var cobranzaCalificacion: EditText
    private lateinit var cobranzaComentario: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte14)



        // Enlazar views
        cobranzaVisita = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitas = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisita = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespacho = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacion = findViewById(R.id.cobranza_calificacion)
        cobranzaComentario = findViewById(R.id.cobranza_comentario)

        btnActualizar = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosCobranza()

        btnActualizar.setOnClickListener {
            actualizarDatosCobranza()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, actualizarFormatoparte15::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun cargarDatosCobranza() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosCobranza(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    cobranzaVisita.setText(datos.cobranza_visita)
                    cobranzaNumeroVisitas.setText(datos.cobranza_numero_visitas)
                    cobranzaUltimaFechaVisita.setText(datos.cobranza_ultima_fecha_visita)
                    cobranzaDespacho.setText(datos.cobranza_despacho)
                    cobranzaCalificacion.setText(datos.cobranza_calificacion)
                    cobranzaComentario.setText(datos.cobranza_comentario)
                } else {
                    Toast.makeText(this@actualizarFormatoparte14, "No se encontraron datos de cobranza", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte14, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosCobranza() {
        val datos = datosCobranza(
            cobranza_visita = cobranzaVisita.text.toString(),
            cobranza_numero_visitas = cobranzaNumeroVisitas.text.toString(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisita.text.toString(),
            cobranza_despacho = cobranzaDespacho.text.toString(),
            cobranza_calificacion = cobranzaCalificacion.text.toString(),
            cobranza_comentario = cobranzaComentario.text.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosCobranza(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte14, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte14, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte14, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
