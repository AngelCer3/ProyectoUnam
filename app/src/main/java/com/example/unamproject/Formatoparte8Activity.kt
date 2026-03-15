package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*

class Formatoparte8Activity : AppCompatActivity() {

    private lateinit var hogarIntegrantesTrabajando: EditText
    private lateinit var solicitanteActivo: EditText
    private lateinit var solicitanteOcupacionActual: EditText
    private lateinit var solicitanteDesempleadoTiempo: EditText
    private lateinit var solicitanteEmpresaPrevia: EditText

    // ANTIGÜEDAD EMPRESA ANTERIOR
    private lateinit var solicitanteAntiguedadAnteriorAnios: EditText
    private lateinit var solicitanteAntiguedadAnteriorMeses: EditText

    private lateinit var institucionTrabajoSolicitante: EditText
    private lateinit var actividadRemuneradaSolicitante: EditText
    private lateinit var contratoLaboralSolicitante: EditText
    private lateinit var solicitanteIngresoMensual: EditText
    private lateinit var solicitanteEmpresa: EditText

    // ANTIGÜEDAD EMPRESA ACTUAL
    private lateinit var solicitanteAntiguedadAnios: EditText
    private lateinit var solicitanteAntiguedadMeses: EditText

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
            guardarDatos()
        }

        btnSiguiente.setOnClickListener {
            irSiguiente()
        }
    }

    private fun initViews() {

        hogarIntegrantesTrabajando = findViewById(R.id.hogar_integrantes_trabajando)
        solicitanteActivo = findViewById(R.id.solicitante_activo)
        solicitanteOcupacionActual = findViewById(R.id.solicitante_ocupacion_actual)
        solicitanteDesempleadoTiempo = findViewById(R.id.solicitante_desempleado_tiempo)
        solicitanteEmpresaPrevia = findViewById(R.id.solicitante_empresa_previa)

        solicitanteAntiguedadAnteriorAnios = findViewById(R.id.solicitante_antiguedad_anios)
        solicitanteAntiguedadAnteriorMeses = findViewById(R.id.solicitante_antiguedad_meses)

        institucionTrabajoSolicitante = findViewById(R.id.institucion_trabajo_solicitante)
        actividadRemuneradaSolicitante = findViewById(R.id.actividad_remunerada_solicitante)
        contratoLaboralSolicitante = findViewById(R.id.contrato_laboral_solicitante)
        solicitanteIngresoMensual = findViewById(R.id.solicitante_ingreso_mensual)
        solicitanteEmpresa = findViewById(R.id.solicitante_empresa)

        solicitanteAntiguedadAnios = findViewById(R.id.antiguedad_anios)
        solicitanteAntiguedadMeses = findViewById(R.id.antiguedad_meses)

        comprobanteIngresosSolicitante = findViewById(R.id.comprobante_ingresos_solicitante)
        institucionCotizacionSolicitante = findViewById(R.id.institucion_cotizacion_solicitante)
        ingresoConceptosSolicitante = findViewById(R.id.ingreso_conceptos_solicitante)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {

        if (solicitanteAntiguedadAnios.text.toString().isBlank() ||
            solicitanteAntiguedadMeses.text.toString().isBlank()
        ) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe ingresar la antigüedad en la empresa actual",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun guardarDatos() {

        val antiguedadAnterior =
            "${solicitanteAntiguedadAnteriorAnios.text} años ${solicitanteAntiguedadAnteriorMeses.text} meses"

        val antiguedadActual =
            "${solicitanteAntiguedadAnios.text} años ${solicitanteAntiguedadMeses.text} meses"

        val datos = datosSolicitante(

            hogar_integrantes_trabajando = hogarIntegrantesTrabajando.text.toString(),
            solicitante_activo = solicitanteActivo.text.toString(),
            solicitante_ocupacion_actual = solicitanteOcupacionActual.text.toString(),
            solicitante_desempleado_tiempo = solicitanteDesempleadoTiempo.text.toString(),
            solicitante_empresa_previa = solicitanteEmpresaPrevia.text.toString(),

            solicitante_antiguedad_trabajo_anterior = antiguedadAnterior,

            institucion_trabajo_solicitante = institucionTrabajoSolicitante.text.toString(),
            actividad_remunerada_solicitante = actividadRemuneradaSolicitante.text.toString(),
            contrato_laboral_solicitante = contratoLaboralSolicitante.text.toString(),
            solicitante_ingreso_mensual = solicitanteIngresoMensual.text.toString(),
            solicitante_empresa = solicitanteEmpresa.text.toString(),

            solicitante_antiguedad = antiguedadActual,

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
                            "Error al guardar los datos",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )

                    }

                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar al servidor",
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