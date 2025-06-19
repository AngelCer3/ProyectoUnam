package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte16)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Vincular vistas
        bindViews()

        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (validarCampos()) {
                irSiguiente()
            }
        }
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

    private fun validarCampos(): Boolean {
        // Validar IDs
        if (idAcreditado.isNullOrBlank() || idUsuario.isNullOrBlank()) {
            mostrarDialogoValidacion(
                "Datos faltantes",
                "Faltan datos del acreditado o usuario",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar campos de vivienda
        if (viviendaNumeroHabitaciones.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar el número de habitaciones",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            viviendaNumeroHabitaciones.requestFocus()
            return false
        }

        if (viviendaNumeroHabitaciones.text.toString().toIntOrNull() == null) {
            mostrarDialogoValidacion(
                "Valor inválido",
                "El número de habitaciones debe ser un valor numérico",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            viviendaNumeroHabitaciones.requestFocus()
            return false
        }

        if (viviendaTipoPiso.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar el tipo de piso",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            viviendaTipoPiso.requestFocus()
            return false
        }

        if (viviendaTipoTecho.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar el tipo de techo",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            viviendaTipoTecho.requestFocus()
            return false
        }

        if (viviendaCuentaBano.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si cuenta con baño",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            viviendaCuentaBano.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ejecutar ambas operaciones en paralelo
                val viviendaDeferred = async { guardarDatosVivienda() }
                val observacionesDeferred = async { guardarObservaciones() }

                // Esperar ambos resultados
                val viviendaResult = viviendaDeferred.await()
                val observacionesResult = observacionesDeferred.await()

                withContext(Dispatchers.Main) {
                    when {
                        viviendaResult && observacionesResult -> {
                            mostrarDialogoValidacion(
                                "Éxito",
                                "Todos los datos se guardaron correctamente",
                                android.R.drawable.ic_dialog_info,
                                0xFF388E3C.toInt()
                            )
                        }
                        viviendaResult -> {
                            mostrarDialogoValidacion(
                                "Advertencia",
                                "Datos de vivienda guardados, pero hubo un error con las observaciones",
                                android.R.drawable.ic_dialog_alert,
                                0xFFFFA000.toInt()
                            )
                        }
                        observacionesResult -> {
                            mostrarDialogoValidacion(
                                "Advertencia",
                                "Observaciones guardadas, pero hubo un error con los datos de vivienda",
                                android.R.drawable.ic_dialog_alert,
                                0xFFFFA000.toInt()
                            )
                        }
                        else -> {
                            mostrarDialogoValidacion(
                                "Error",
                                "No se pudieron guardar los datos",
                                android.R.drawable.stat_notify_error,
                                0xFFD32F2F.toInt()
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar al servidor: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private suspend fun guardarDatosVivienda(): Boolean {
        val datosVivienda = datosEspecificosVivienda(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        return try {
            val response = RetrofitClient.webService.agregarDatosEspecificosVivienda(datosVivienda)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun guardarObservaciones(): Boolean {
        val datosObs = datosObservaciones(
            observaciones_entrevistador = observacionesEntrevistador.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        return try {
            val response = RetrofitClient.webService.agregarDatosObservaciones(datosObs)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, FormatoparteFinal::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }

    private fun mostrarDialogoValidacion(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val icon = view.findViewById<ImageView>(R.id.ivIcon)
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val message = view.findViewById<TextView>(R.id.tvMessage)
        val btnOk = view.findViewById<Button>(R.id.btnOk)

        icon.setImageResource(iconoResId)
        icon.setColorFilter(colorTitulo)
        title.text = titulo
        title.setTextColor(colorTitulo)
        message.text = mensaje

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
            onAceptar?.invoke()
        }

        dialog.show()
    }
}