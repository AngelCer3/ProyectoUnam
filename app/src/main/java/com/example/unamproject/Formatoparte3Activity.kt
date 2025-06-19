package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte3Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

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

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte3)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
            if (validarCampos()) {
                siguienteFormato()
            }
        }
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

        siguiente = findViewById(R.id.btnSiguiente)
        guardar = findViewById(R.id.btnGuardar)
    }

    private fun validarCampos(): Boolean {
        val camposObligatorios = listOf(
            Pair(viviendaLocalizada, "Localización de la vivienda"),
            Pair(viviendaHabitada, "Habitabilidad de la vivienda"),
            Pair(verificacionMetodo, "Método de verificación"),
            Pair(vecinoNombre, "Nombre del vecino"),
            Pair(vecinoDireccion, "Dirección del vecino"),
            Pair(acreditadoVive, "¿El acreditado vive ahí?"),
            Pair(jefeFamiliaNombre, "Nombre del jefe de familia"),
            Pair(jefeFamiliaRelacion, "Relación con el jefe de familia"),
            Pair(fechaOcupacion, "Fecha de ocupación"),
            Pair(documentoTraspaso, "Documento de traspaso"),
            Pair(tipoDocumentoTraspaso, "Tipo de documento de traspaso"),
            Pair(documentoMostrado, "Documento mostrado"),
            Pair(documentoCopiaEntregada, "Copia del documento entregada")
        )

        for ((campo, nombreCampo) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, completa el campo: $nombreCampo",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        if (situacionVivienda.selectedItem == null || situacionVivienda.selectedItem.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Por favor, selecciona la situación de la vivienda",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            situacionVivienda.requestFocus()
            return false
        }

        if (idAcreditado.isNullOrBlank() || idUsuario.isNullOrBlank()) {
            mostrarDialogoValidacion(
                "Datos faltantes",
                "Faltan datos del acreditado o usuario",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val datosVivienda = datosVivienda(
            vivienda_localizada = viviendaLocalizada.text.toString(),
            vivienda_habitada = viviendaHabitada.text.toString(),
            verificacion_metodo = verificacionMetodo.text.toString(),
            verificacion_otro = verificacionOtro.text.toString(),
            vecino_nombre = vecinoNombre.text.toString(),
            vecino_direccion = vecinoDireccion.text.toString(),
            acreditado_vive = acreditadoVive.text.toString(),
            jefe_familia_nombre = jefeFamiliaNombre.text.toString(),
            jefe_familia_relacion = jefeFamiliaRelacion.text.toString(),
            fecha_ocupacion = fechaOcupacion.text.toString(),
            situacion_vivienda = situacionVivienda.selectedItem.toString(),
            documento_traspaso = documentoTraspaso.text.toString(),
            tipo_documento_traspaso = tipoDocumentoTraspaso.text.toString(),
            documento_mostrado = documentoMostrado.text.toString(),
            documento_copia_entregada = documentoCopiaEntregada.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosVivienda(datosVivienda)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos guardados exitosamente",
                            android.R.drawable.ic_dialog_info,
                            0xFF388E3C.toInt()
                        )
                    } else {
                        mostrarDialogoValidacion(
                            "Error",
                            "Error al guardar datos: ${response.message()}",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte4Activity::class.java)
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