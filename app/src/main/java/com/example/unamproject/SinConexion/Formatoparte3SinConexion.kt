package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class Formatoparte3SinConexion : AppCompatActivity() {

    private lateinit var guardar: Button
    private lateinit var siguiente: Button

    private lateinit var viviendaLocalizada: EditText
    private lateinit var viviendaHabitada: EditText
    private lateinit var verificacionMetodo: EditText
    private lateinit var verificacionOtro: EditText
    private lateinit var vecinoNombre: EditText
    private lateinit var vecinoDireccion: EditText
    private lateinit var acreditadoVive: EditText
    private lateinit var jefeFamiliaNombre: EditText
    private lateinit var jefeFamiliaRelacion: EditText
    private lateinit var fechaOcupacion: EditText
    private lateinit var situacionVivienda: Spinner
    private lateinit var documentoTraspaso: EditText
    private lateinit var tipoDocumentoTraspaso: EditText
    private lateinit var documentoMostrado: EditText
    private lateinit var documentoCopiaEntregada: EditText

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte3_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        // Inicializar base de datos
        database = AppDatabase.getDatabase(this)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        viviendaLocalizada = findViewById(R.id.vivienda_localizada)
        viviendaHabitada = findViewById(R.id.vivienda_habitada)
        verificacionMetodo = findViewById(R.id.verificacion_metodo)
        verificacionOtro = findViewById(R.id.verificacion_otro)
        vecinoNombre = findViewById(R.id.vecino_nombre)
        vecinoDireccion = findViewById(R.id.vecino_direccion)
        acreditadoVive = findViewById(R.id.acreditado_vive)
        jefeFamiliaNombre = findViewById(R.id.jefe_familia_nombre)
        jefeFamiliaRelacion = findViewById(R.id.jefe_familia_relacion)
        fechaOcupacion = findViewById(R.id.fecha_ocupacion)
        situacionVivienda = findViewById(R.id.situacion_vivienda)
        documentoTraspaso = findViewById(R.id.documento_traspaso)
        tipoDocumentoTraspaso = findViewById(R.id.tipo_documento_traspaso)
        documentoMostrado = findViewById(R.id.documento_mostrado)
        documentoCopiaEntregada = findViewById(R.id.documento_copia_entregada)

        guardar = findViewById(R.id.btnGuardar)
        siguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
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
        // Campos obligatorios
        val camposRequeridos = listOf(
            viviendaLocalizada to "Vivienda localizada",
            viviendaHabitada to "Vivienda habitada",
            verificacionMetodo to "Método de verificación",
            acreditadoVive to "Acreditado vive en la vivienda",
            jefeFamiliaNombre to "Nombre del jefe de familia",
            situacionVivienda to "Situación de la vivienda"
        )

        for ((campo, nombre) in camposRequeridos) {
            if (campo is EditText && campo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "El campo $nombre es obligatorio",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar fecha de ocupación (si está presente)
        val fechaStr = fechaOcupacion.text.toString().trim()
        if (fechaStr.isNotEmpty()) {
            try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaStr)
            } catch (e: Exception) {
                mostrarDialogo(
                    titulo = "Fecha inválida",
                    mensaje = "La fecha de ocupación debe tener formato dd/MM/yyyy",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                fechaOcupacion.requestFocus()
                return false
            }
        }

        // Validar que si se seleccionó "Otro" en método de verificación, se especifique
        if (verificacionMetodo.text.toString().trim().equals("Otro", ignoreCase = true) &&
            verificacionOtro.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Especificación requerida",
                mensaje = "Debe especificar el método de verificación cuando selecciona 'Otro'",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            verificacionOtro.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val datos = DatosViviendaEntity(
            vivienda_localizada = viviendaLocalizada.text.toString().trim(),
            vivienda_habitada = viviendaHabitada.text.toString().trim(),
            verificacion_metodo = verificacionMetodo.text.toString().trim(),
            verificacion_otro = verificacionOtro.text.toString().trim(),
            vecino_nombre = vecinoNombre.text.toString().trim(),
            vecino_direccion = vecinoDireccion.text.toString().trim(),
            acreditado_vive = acreditadoVive.text.toString().trim(),
            jefe_familia_nombre = jefeFamiliaNombre.text.toString().trim(),
            jefe_familia_relacion = jefeFamiliaRelacion.text.toString().trim(),
            fecha_ocupacion = fechaOcupacion.text.toString().trim(),
            situacion_vivienda = situacionVivienda.selectedItem.toString(),
            documento_traspaso = documentoTraspaso.text.toString().trim(),
            tipo_documento_traspaso = tipoDocumentoTraspaso.text.toString().trim(),
            documento_mostrado = documentoMostrado.text.toString().trim(),
            documento_copia_entregada = documentoCopiaEntregada.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosViviendaDao().insertDatosVivienda(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de vivienda guardados correctamente",
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
        val intent = Intent(this, Formatoparte4SinConexion::class.java)
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