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

class Formatoparte11SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts
    private lateinit var gastoDespensaAlimentacion: EditText
    private lateinit var gastoDespensaMotivo: EditText
    private lateinit var gastoGas: EditText
    private lateinit var gastoGasMotivo: EditText
    private lateinit var gastoLuz: EditText
    private lateinit var gastoLuzMotivo: EditText
    private lateinit var gastoAgua: EditText
    private lateinit var gastoAguaMotivo: EditText
    private lateinit var gastoServicioTelefonico: EditText
    private lateinit var gastoServicioTelefonicoMotivo: EditText
    private lateinit var gastoMantenimientoVivienda: EditText
    private lateinit var gastoMantenimientoMotivo: EditText
    private lateinit var gastoTransportePublico: EditText
    private lateinit var gastoTransporteMotivo: EditText
    private lateinit var gastoGasolina: EditText
    private lateinit var gastoGasolinaMotivo: EditText
    private lateinit var gastoServiciosSalud: EditText
    private lateinit var gastoSaludMotivo: EditText
    private lateinit var gastoEducacion: EditText
    private lateinit var gastoEducacionMotivo: EditText
    private lateinit var gastoRecreacion: EditText
    private lateinit var gastoRecreacionMotivo: EditText
    private lateinit var gastoComidasFuera: EditText
    private lateinit var gastoComidasFueraMotivo: EditText
    private lateinit var gastoVestidoCalzado: EditText
    private lateinit var gastoVestidoCalzadoMotivo: EditText
    private lateinit var gastoPensionVehiculo: EditText
    private lateinit var gastoPensionVehiculoMotivo: EditText
    private lateinit var gastoTelefonoCelular: EditText
    private lateinit var gastoTelefonoCelularMotivo: EditText
    private lateinit var gastoTelevisionPago: EditText
    private lateinit var gastoTelevisionPagoMotivo: EditText
    private lateinit var gastoPagoCreditos: EditText
    private lateinit var gastoPagoCreditosMotivo: EditText
    private lateinit var gastoOtrosDescripcion: EditText
    private lateinit var gastoOtrosMotivo: EditText
    private lateinit var gastoMetodoPago: EditText

    // Botones
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte11_sin_conexion)

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
        // Inicializar EditTexts
        gastoDespensaAlimentacion = findViewById(R.id.gasto_despensa_alimentacion)
        gastoDespensaMotivo = findViewById(R.id.gasto_despensa_motivo)
        gastoGas = findViewById(R.id.gasto_gas)
        gastoGasMotivo = findViewById(R.id.gasto_gas_motivo)
        gastoLuz = findViewById(R.id.gasto_luz)
        gastoLuzMotivo = findViewById(R.id.gasto_luz_motivo)
        gastoAgua = findViewById(R.id.gasto_agua)
        gastoAguaMotivo = findViewById(R.id.gasto_agua_motivo)
        gastoServicioTelefonico = findViewById(R.id.gasto_servicio_telefonico)
        gastoServicioTelefonicoMotivo = findViewById(R.id.gasto_servicio_telefonico_motivo)
        gastoMantenimientoVivienda = findViewById(R.id.gasto_mantenimiento_vivienda)
        gastoMantenimientoMotivo = findViewById(R.id.gasto_mantenimiento_motivo)
        gastoTransportePublico = findViewById(R.id.gasto_transporte_publico)
        gastoTransporteMotivo = findViewById(R.id.gasto_transporte_motivo)
        gastoGasolina = findViewById(R.id.gasto_gasolina)
        gastoGasolinaMotivo = findViewById(R.id.gasto_gasolina_motivo)
        gastoServiciosSalud = findViewById(R.id.gasto_servicios_salud)
        gastoSaludMotivo = findViewById(R.id.gasto_salud_motivo)
        gastoEducacion = findViewById(R.id.gasto_educacion)
        gastoEducacionMotivo = findViewById(R.id.gasto_educacion_motivo)
        gastoRecreacion = findViewById(R.id.gasto_recreacion)
        gastoRecreacionMotivo = findViewById(R.id.gasto_recreacion_motivo)
        gastoComidasFuera = findViewById(R.id.gasto_comidas_fuera)
        gastoComidasFueraMotivo = findViewById(R.id.gasto_comidas_fuera_motivo)
        gastoVestidoCalzado = findViewById(R.id.gasto_vestido_calzado)
        gastoVestidoCalzadoMotivo = findViewById(R.id.gasto_vestido_calzado_motivo)
        gastoPensionVehiculo = findViewById(R.id.gasto_pension_vehiculo)
        gastoPensionVehiculoMotivo = findViewById(R.id.gasto_pension_vehiculo_motivo)
        gastoTelefonoCelular = findViewById(R.id.gasto_telefono_celular)
        gastoTelefonoCelularMotivo = findViewById(R.id.gasto_telefono_celular_motivo)
        gastoTelevisionPago = findViewById(R.id.gasto_television_pago)
        gastoTelevisionPagoMotivo = findViewById(R.id.gasto_television_pago_motivo)
        gastoPagoCreditos = findViewById(R.id.gasto_pago_creditos)
        gastoPagoCreditosMotivo = findViewById(R.id.gasto_pago_creditos_motivo)
        gastoOtrosDescripcion = findViewById(R.id.gasto_otros_descripcion)
        gastoOtrosMotivo = findViewById(R.id.gasto_otros_motivo)
        gastoMetodoPago = findViewById(R.id.gasto_metodo_pago)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosGastos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosGastos() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar que los montos sean numéricos y no negativos
        val camposMontos = listOf(
            gastoDespensaAlimentacion to "Gasto en despensa/alimentación",
            gastoGas to "Gasto en gas",
            gastoLuz to "Gasto en luz",
            gastoAgua to "Gasto en agua",
            gastoServicioTelefonico to "Gasto en servicio telefónico",
            gastoMantenimientoVivienda to "Gasto en mantenimiento de vivienda",
            gastoTransportePublico to "Gasto en transporte público",
            gastoGasolina to "Gasto en gasolina",
            gastoServiciosSalud to "Gasto en servicios de salud",
            gastoEducacion to "Gasto en educación",
            gastoRecreacion to "Gasto en recreación",
            gastoComidasFuera to "Gasto en comidas fuera",
            gastoVestidoCalzado to "Gasto en vestido y calzado",
            gastoPensionVehiculo to "Gasto en pensión vehicular",
            gastoTelefonoCelular to "Gasto en teléfono celular",
            gastoTelevisionPago to "Gasto en televisión de pago",
            gastoPagoCreditos to "Gasto en pago de créditos"
        )

        for ((campo, nombre) in camposMontos) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty()) {
                try {
                    val monto = valor.toDouble()
                    if (monto < 0) {
                        mostrarDialogo(
                            titulo = "Valor inválido",
                            mensaje = "El $nombre no puede ser negativo",
                            iconoResId = android.R.drawable.ic_dialog_alert,
                            colorTitulo = 0xFFD32F2F.toInt()
                        )
                        campo.requestFocus()
                        return false
                    }
                } catch (e: NumberFormatException) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "El $nombre debe ser un valor numérico",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar campos obligatorios
        val camposRequeridos = listOf(
            gastoMetodoPago to "Método de pago principal"
        )

        for ((campo, nombre) in camposRequeridos) {
            if (campo.text.toString().trim().isEmpty()) {
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

        // Validar que si hay gasto en "otros", se especifique descripción
        if (gastoOtrosDescripcion.text.toString().trim().isNotEmpty() &&
            gastoOtrosMotivo.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar el motivo del gasto en 'Otros'",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            gastoOtrosMotivo.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatosGastos() {
        val datos = DatosGastosEntity(
            gasto_despensa_alimentacion = gastoDespensaAlimentacion.text.toString().trim(),
            gasto_despensa_motivo = gastoDespensaMotivo.text.toString().trim(),
            gasto_gas = gastoGas.text.toString().trim(),
            gasto_gas_motivo = gastoGasMotivo.text.toString().trim(),
            gasto_luz = gastoLuz.text.toString().trim(),
            gasto_luz_motivo = gastoLuzMotivo.text.toString().trim(),
            gasto_agua = gastoAgua.text.toString().trim(),
            gasto_agua_motivo = gastoAguaMotivo.text.toString().trim(),
            gasto_servicio_telefonico = gastoServicioTelefonico.text.toString().trim(),
            gasto_servicio_telefonico_motivo = gastoServicioTelefonicoMotivo.text.toString().trim(),
            gasto_mantenimiento_vivienda = gastoMantenimientoVivienda.text.toString().trim(),
            gasto_mantenimiento_motivo = gastoMantenimientoMotivo.text.toString().trim(),
            gasto_transporte_publico = gastoTransportePublico.text.toString().trim(),
            gasto_transporte_motivo = gastoTransporteMotivo.text.toString().trim(),
            gasto_gasolina = gastoGasolina.text.toString().trim(),
            gasto_gasolina_motivo = gastoGasolinaMotivo.text.toString().trim(),
            gasto_servicios_salud = gastoServiciosSalud.text.toString().trim(),
            gasto_salud_motivo = gastoSaludMotivo.text.toString().trim(),
            gasto_educacion = gastoEducacion.text.toString().trim(),
            gasto_educacion_motivo = gastoEducacionMotivo.text.toString().trim(),
            gasto_recreacion = gastoRecreacion.text.toString().trim(),
            gasto_recreacion_motivo = gastoRecreacionMotivo.text.toString().trim(),
            gasto_comidas_fuera = gastoComidasFuera.text.toString().trim(),
            gasto_comidas_fuera_motivo = gastoComidasFueraMotivo.text.toString().trim(),
            gasto_vestido_calzado = gastoVestidoCalzado.text.toString().trim(),
            gasto_vestido_calzado_motivo = gastoVestidoCalzadoMotivo.text.toString().trim(),
            gasto_pension_vehiculo = gastoPensionVehiculo.text.toString().trim(),
            gasto_pension_vehiculo_motivo = gastoPensionVehiculoMotivo.text.toString().trim(),
            gasto_telefono_celular = gastoTelefonoCelular.text.toString().trim(),
            gasto_telefono_celular_motivo = gastoTelefonoCelularMotivo.text.toString().trim(),
            gasto_television_pago = gastoTelevisionPago.text.toString().trim(),
            gasto_television_pago_motivo = gastoTelevisionPagoMotivo.text.toString().trim(),
            gasto_pago_creditos = gastoPagoCreditos.text.toString().trim(),
            gasto_pago_creditos_motivo = gastoPagoCreditosMotivo.text.toString().trim(),
            gasto_otros_descripcion = gastoOtrosDescripcion.text.toString().trim(),
            gasto_otros_motivo = gastoOtrosMotivo.text.toString().trim(),
            gasto_metodo_pago = gastoMetodoPago.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosGastosDao().insertDatosGastos(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de gastos guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de gastos: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte12SinConexion::class.java)
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