package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte16SinConexion : AppCompatActivity() {

    private lateinit var viviendaNumeroHabitaciones: EditText
    private lateinit var viviendaTipoPiso: EditText
    private lateinit var viviendaTipoPisoOtro: EditText
    private lateinit var viviendaTipoTecho: EditText
    private lateinit var viviendaCuentaBano: EditText
    private lateinit var observacionesEntrevistador: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var db: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte16_sin_conexion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        db = AppDatabase.getDatabase(this)
        initViews()
        setupButtons()
    }

    private fun initViews() {
        viviendaNumeroHabitaciones = findViewById(R.id.vivienda_numero_habitaciones)
        viviendaTipoPiso = findViewById(R.id.vivienda_tipo_piso)
        viviendaTipoPisoOtro = findViewById(R.id.vivienda_tipo_piso_otro)
        viviendaTipoTecho = findViewById(R.id.vivienda_tipo_techo)
        viviendaCuentaBano = findViewById(R.id.viviendo_cuenta_bano)
        observacionesEntrevistador = findViewById(R.id.observaciones_entrevistador)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatos() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar número de habitaciones
        val numHabitaciones = viviendaNumeroHabitaciones.text.toString().trim()
        if (numHabitaciones.isNotEmpty()) {
            try {
                val num = numHabitaciones.toInt()
                if (num < 0) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "El número de habitaciones no puede ser negativo",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    viviendaNumeroHabitaciones.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "El número de habitaciones debe ser un valor numérico",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                viviendaNumeroHabitaciones.requestFocus()
                return false
            }
        }

        // Validar tipo de piso
        if (viviendaTipoPiso.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar el tipo de piso de la vivienda",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            viviendaTipoPiso.requestFocus()
            return false
        }

        // Validar campo "otro" si se seleccionó "Otro" en tipo de piso
        if (viviendaTipoPiso.text.toString().trim().equals("Otro", ignoreCase = true) &&
            viviendaTipoPisoOtro.text.toString().trim().isEmpty()
        ) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar el tipo de piso (otro)",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            viviendaTipoPisoOtro.requestFocus()
            return false
        }

        // Validar tipo de techo
        if (viviendaTipoTecho.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar el tipo de techo de la vivienda",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            viviendaTipoTecho.requestFocus()
            return false
        }

        // Validar si cuenta con baño
        if (viviendaCuentaBano.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar si la vivienda cuenta con baño",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            viviendaCuentaBano.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val datosVivienda = DatosEspecificosViviendaEntity(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString().trim(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString().trim(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString().trim(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString().trim(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        val datosObservaciones = DatosObservacionesEntity(
            observaciones_entrevistador = observacionesEntrevistador.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    db.datosEspecificosViviendaDao().insertDatosEspecificosVivienda(datosVivienda)
                    db.datosObservacionesDao().insertDatosObservaciones(datosObservaciones)
                }

                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de vivienda y observaciones guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, FormatoparteFinalSinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado.toString())
        startActivity(intent)
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorYCerrar(mensaje: String) {
        mostrarDialogo(
            titulo = "Error crítico",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        ) {
            finish()
        }
    }

    private fun mostrarDialogo(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)

        view.findViewById<ImageView>(R.id.ivIcon).apply {
            setImageResource(iconoResId)
            setColorFilter(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvTitle).apply {
            text = titulo
            setTextColor(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvMessage).text = mensaje

        AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
            .apply {
                view.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    dismiss()
                    onAceptar?.invoke()
                }
                show()
            }
    }
}