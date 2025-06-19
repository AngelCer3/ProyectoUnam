package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte8Activity : AppCompatActivity() {

    private lateinit var hogarIntegrantesTrabajando: EditText
    private lateinit var solicitanteActivo: EditText
    private lateinit var solicitanteOcupacionActual: EditText
    private lateinit var solicitanteDesempleadoTiempo: EditText
    private lateinit var solicitanteEmpresaPrevia: EditText
    private lateinit var solicitanteAntiguedadEmpleoAnterior: EditText
    private lateinit var institucionTrabajoSolicitante: EditText
    private lateinit var actividadRemuneradaSolicitante: EditText
    private lateinit var contratoLaboralSolicitante: EditText
    private lateinit var solicitanteIngresoMensual: EditText
    private lateinit var solicitanteEmpresa: EditText
    private lateinit var solicitanteAntiguedad: EditText
    private lateinit var comprobanteIngresosSolicitante: EditText
    private lateinit var institucionCotizacionSolicitante: EditText
    private lateinit var ingresoConceptosSolicitante: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte8)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

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

    private fun initViews() {
        hogarIntegrantesTrabajando = findViewById(R.id.hogar_integrantes_trabajando)
        solicitanteActivo = findViewById(R.id.solicitante_activo)
        solicitanteOcupacionActual = findViewById(R.id.solicitante_ocupacion_actual)
        solicitanteDesempleadoTiempo = findViewById(R.id.solicitante_desempleado_tiempo)
        solicitanteEmpresaPrevia = findViewById(R.id.solicitante_empresa_previa)
        solicitanteAntiguedadEmpleoAnterior = findViewById(R.id.solicitante_antiguedad_empleo_anterior)
        institucionTrabajoSolicitante = findViewById(R.id.institucion_trabajo_solicitante)
        actividadRemuneradaSolicitante = findViewById(R.id.actividad_remunerada_solicitante)
        contratoLaboralSolicitante = findViewById(R.id.contrato_laboral_solicitante)
        solicitanteIngresoMensual = findViewById(R.id.solicitante_ingreso_mensual)
        solicitanteEmpresa = findViewById(R.id.solicitante_empresa)
        solicitanteAntiguedad = findViewById(R.id.solicitante_antiguedad)
        comprobanteIngresosSolicitante = findViewById(R.id.comprobante_ingresos_solicitante)
        institucionCotizacionSolicitante = findViewById(R.id.institucion_cotizacion_solicitante)
        ingresoConceptosSolicitante = findViewById(R.id.ingreso_conceptos_solicitante)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Campos obligatorios principales
        val camposObligatorios = listOf(
            Pair(hogarIntegrantesTrabajando, "Integrantes del hogar que trabajan"),
            Pair(solicitanteActivo, "¿El solicitante está activo laboralmente?"),
            Pair(solicitanteOcupacionActual, "Ocupación actual del solicitante"),
            Pair(solicitanteIngresoMensual, "Ingreso mensual del solicitante"),
            Pair(solicitanteEmpresa, "Empresa del solicitante"),
            Pair(solicitanteAntiguedad, "Antigüedad del solicitante")
        )

        for ((campo, nombre) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, complete el campo: $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar campos numéricos
        val camposNumericos = listOf(
            Pair(hogarIntegrantesTrabajando, "Integrantes del hogar que trabajan"),
            Pair(solicitanteIngresoMensual, "Ingreso mensual del solicitante"),
            Pair(solicitanteAntiguedad, "Antigüedad del solicitante")
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().isNotBlank() && campo.text.toString().toDoubleOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El campo '$nombre' debe ser numérico",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación condicional si el solicitante está inactivo
        if (solicitanteActivo.text.toString().equals("No", ignoreCase = true)) {
            if (solicitanteDesempleadoTiempo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar el tiempo que lleva desempleado",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                solicitanteDesempleadoTiempo.requestFocus()
                return false
            }

            if (solicitanteEmpresaPrevia.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la empresa previa",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                solicitanteEmpresaPrevia.requestFocus()
                return false
            }

            if (solicitanteAntiguedadEmpleoAnterior.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la antigüedad en el empleo anterior",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                solicitanteAntiguedadEmpleoAnterior.requestFocus()
                return false
            }
        }

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

        return true
    }

    private fun guardarDatos() {
        val datos = datosSolicitante(
            hogar_integrantes_trabajando = hogarIntegrantesTrabajando.text.toString(),
            solicitante_activo = solicitanteActivo.text.toString(),
            solicitante_ocupacion_actual = solicitanteOcupacionActual.text.toString(),
            solicitante_desempleado_tiempo = solicitanteDesempleadoTiempo.text.toString(),
            solicitante_empresa_previa = solicitanteEmpresaPrevia.text.toString(),
            solicitante_antiguedad_trabajo_anterior = solicitanteAntiguedadEmpleoAnterior.text.toString(),
            institucion_trabajo_solicitante = institucionTrabajoSolicitante.text.toString(),
            actividad_remunerada_solicitante = actividadRemuneradaSolicitante.text.toString(),
            contrato_laboral_solicitante = contratoLaboralSolicitante.text.toString(),
            solicitante_ingreso_mensual = solicitanteIngresoMensual.text.toString(),
            solicitante_empresa = solicitanteEmpresa.text.toString(),
            solicitante_antiguedad = solicitanteAntiguedad.text.toString(),
            comprobante_ingresos_solicitante = comprobanteIngresosSolicitante.text.toString(),
            institucion_cotizacion_solicitante = institucionCotizacionSolicitante.text.toString(),
            ingresos_conceptos_solicitante = ingresoConceptosSolicitante.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosSolicitante(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos del solicitante guardados correctamente",
                            android.R.drawable.ic_dialog_info,
                            0xFF388E3C.toInt()
                        )
                    } else {
                        mostrarDialogoValidacion(
                            "Error",
                            "Error al guardar los datos: ${response.message()}",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )
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

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte9Activity::class.java)
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