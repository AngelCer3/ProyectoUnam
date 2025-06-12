package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.MenuTrabajadorActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosEspecificosVivienda
import com.example.unamproject.datosObservaciones
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class actualizarFormatoparte16 : AppCompatActivity() {

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    private lateinit var viviendaNumeroHabitaciones: EditText
    private lateinit var viviendaTipoPiso: EditText
    private lateinit var viviendaTipoPisoOtro: EditText
    private lateinit var viviendaTipoTecho: EditText
    private lateinit var viviendaCuentaBano: EditText
    private lateinit var observacionesEntrevistador: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte16)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: return
        idUsuario = intent.getStringExtra("id_usuario")

        // Vincular vistas
        viviendaNumeroHabitaciones = findViewById(R.id.vivienda_numero_habitaciones)
        viviendaTipoPiso = findViewById(R.id.vivienda_tipo_piso)
        viviendaTipoPisoOtro = findViewById(R.id.vivienda_tipo_piso_otro)
        viviendaTipoTecho = findViewById(R.id.vivienda_tipo_techo)
        viviendaCuentaBano = findViewById(R.id.viviendo_cuenta_bano)
        observacionesEntrevistador = findViewById(R.id.observaciones_entrevistador)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        // Obtener datos actuales
        obtenerDatos()

        // Botón Actualizar
        btnActualizar.setOnClickListener {
            actualizarDatos()
        }

        // Botón Siguiente
        btnSiguiente.setOnClickListener {
            val intent = Intent(this,actualizarFormatoparteFinal::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }
    }

    private fun obtenerDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseVivienda = RetrofitClient.webService.obtenerDatosEspecificosVivienda(idAcreditado)
            val responseObs = RetrofitClient.webService.obtenerDatosObservaciones(idAcreditado)

            withContext(Dispatchers.Main) {
                if (responseVivienda.isSuccessful && responseVivienda.body() != null) {
                    val data = responseVivienda.body()!!
                    viviendaNumeroHabitaciones.setText(data.vivienda_numero_habitaciones)
                    viviendaTipoPiso.setText(data.vivienda_tipo_piso)
                    viviendaTipoPisoOtro.setText(data.vivienda_tipo_piso_otro)
                    viviendaTipoTecho.setText(data.vivienda_tipo_techo)
                    viviendaCuentaBano.setText(data.vivienda_cuenta_bano)
                }

                if (responseObs.isSuccessful && responseObs.body() != null) {
                    observacionesEntrevistador.setText(responseObs.body()!!.observaciones_entrevistador)
                }
            }
        }
    }

    private fun actualizarDatos() {
        val viviendaData = datosEspecificosVivienda(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        val observacionesData = datosObservaciones(
            observaciones_entrevistador = observacionesEntrevistador.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            val responseVivienda = RetrofitClient.webService.actualizarDatosEspecificosVivienda(idAcreditado, viviendaData)
            val responseObs = RetrofitClient.webService.actualizarDatosObservaciones(idAcreditado, observacionesData)

            withContext(Dispatchers.Main) {
                if (responseVivienda.isSuccessful && responseObs.isSuccessful) {
                    val bodyVivienda = responseVivienda.body()
                    val bodyObs = responseObs.body()

                    if (bodyVivienda != null && bodyObs != null) {
                        Toast.makeText(
                            this@actualizarFormatoparte16,
                            "Datos Actualizados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@actualizarFormatoparte16, "Respuesta vacía del servidor", Toast.LENGTH_LONG).show()
                    }

                } else {
                    val errorMsgVivienda = responseVivienda.errorBody()?.string()
                    val errorMsgObs = responseObs.errorBody()?.string()

                    Toast.makeText(
                        this@actualizarFormatoparte16,
                        "Error al actualizar datos:\nVivienda: $errorMsgVivienda\nObservaciones: $errorMsgObs",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
