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
    private var idUsuario:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte16)

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")


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

        // Guardar datos de vivienda y observaciones en paralelo para mejor UX
        guardarDatosVivienda()
        guardarObservaciones()
    }

    private fun guardarDatosVivienda() {
        val datosVivienda = datosEspecificosVivienda(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseVivienda = RetrofitClient.webService.agregarDatosEspecificosVivienda(datosVivienda)
                withContext(Dispatchers.Main) {
                    if (responseVivienda.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Datos de vivienda guardados correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val errorMsg = responseVivienda.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Error al guardar vivienda: $errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte16Activity,
                        "Error de conexión al guardar vivienda: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun guardarObservaciones() {
        val datosObs = datosObservaciones(
            observaciones_entrevistador = observacionesEntrevistador.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseObs = RetrofitClient.webService.agregarDatosObservaciones(datosObs)
                withContext(Dispatchers.Main) {
                    if (responseObs.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Observaciones guardadas correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val errorBody = responseObs.errorBody()?.string() ?: "Sin detalles"
                        Toast.makeText(
                            this@Formatoparte16Activity,
                            "Error al guardar observaciones: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte16Activity,
                        "Error de conexión al guardar observaciones: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, FormatoparteFinal::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario",idUsuario)
        startActivity(intent)
    }
}
