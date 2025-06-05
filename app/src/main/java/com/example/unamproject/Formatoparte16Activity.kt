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

class Formatoparte16Activity : AppCompatActivity() {

    // Campos de Vivienda
    private lateinit var viviendaNumeroHabitaciones: EditText
    private lateinit var viviendaTipoPiso: EditText
    private lateinit var viviendaTipoPisoOtro: EditText
    private lateinit var viviendaTipoTecho: EditText
    private lateinit var viviendaCuentaBano: EditText

    // Campos de Observaciones
    private lateinit var observacionesEntrevistador: EditText

    // Botones
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte16)

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")

        // Vincular vistas
        bindViews()

        btnGuardar.setOnClickListener { guardarDatos() }
        btnSiguiente.setOnClickListener { irSiguiente() }
    }

    private fun bindViews() {
        // Vivienda
        viviendaNumeroHabitaciones = findViewById(R.id.vivienda_numero_habitaciones)
        viviendaTipoPiso = findViewById(R.id.vivienda_tipo_piso)
        viviendaTipoPisoOtro = findViewById(R.id.vivienda_tipo_piso_otro)
        viviendaTipoTecho = findViewById(R.id.vivienda_tipo_techo)
        viviendaCuentaBano = findViewById(R.id.viviendo_cuenta_bano)

        // Observaciones
        observacionesEntrevistador = findViewById(R.id.observaciones_entrevistador)

        // Botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun guardarDatos() {
        if (idAcreditado.isNullOrEmpty()) {
            Toast.makeText(this, "Error: ID de acreditado no disponible", Toast.LENGTH_LONG).show()
            return
        }

        // Crear ambos objetos de datos
        val datosVivienda = datosEspecificosVivienda(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString(),
            id_acreditado = idAcreditado!!
        )

        val datosObs = datosObservaciones(
            observaciones_entrevistador = observacionesEntrevistador.text.toString(),
            id_acreditado = idAcreditado!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Enviar ambos conjuntos de datos al servidor
                val responseVivienda = RetrofitClient.webService.agregarDatosEspecificosVivienda(datosVivienda)
                val responseObs = RetrofitClient.webService.agregarDatosObservaciones(datosObs)

                withContext(Dispatchers.Main) {
                    if (responseVivienda.isSuccessful && responseObs.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Datos guardados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val errorMsg = buildString {
                            if (!responseVivienda.isSuccessful) append("Error en vivienda. ")
                            if (!responseObs.isSuccessful) append("Error en observaciones.")
                        }
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Error al guardar: $errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte16Activity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, MenuTrabajadorActivity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
        finish()
    }
}