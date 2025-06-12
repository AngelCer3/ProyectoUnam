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

class Formatoparte14Activity : AppCompatActivity() {

    private lateinit var cobranzaVisita: EditText
    private lateinit var cobranzaNumeroVisitas: EditText
    private lateinit var cobranzaUltimaFechaVisita: EditText
    private lateinit var cobranzaDespacho: EditText
    private lateinit var cobranzaCalificacion: EditText
    private lateinit var cobranzaComentario: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte14)

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")


        // Vincular vistas
        cobranzaVisita = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitas = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisita = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespacho = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacion = findViewById(R.id.cobranza_calificacion)
        cobranzaComentario = findViewById(R.id.cobranza_comentario)

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

        val datos = datosCobranza(
            cobranza_visita = cobranzaVisita.text.toString(),
            cobranza_numero_visitas = cobranzaNumeroVisitas.text.toString(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisita.text.toString(),
            cobranza_despacho = cobranzaDespacho.text.toString(),
            cobranza_calificacion = cobranzaCalificacion.text.toString(),
            cobranza_comentario = cobranzaComentario.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosCobranza(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte14Activity,
                            "Datos guardados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@Formatoparte14Activity,
                            "Error al guardar los datos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte14Activity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte15Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}