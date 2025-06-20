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

class Formatoparte11Activity : AppCompatActivity() {

    // Declaración de todos los EditText para gastos
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

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte11)
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

    private fun validarCampos(): Boolean {
        // Lista de pares (campo, nombre) para gastos monetarios
        val gastosMonetarios = listOf(
            Pair(gastoDespensaAlimentacion, "Despensa y alimentación"),
            Pair(gastoGas, "Gas"),
            Pair(gastoLuz, "Luz"),
            Pair(gastoAgua, "Agua"),
            Pair(gastoServicioTelefonico, "Servicio telefónico"),
            Pair(gastoMantenimientoVivienda, "Mantenimiento vivienda"),
            Pair(gastoTransportePublico, "Transporte público"),
            Pair(gastoGasolina, "Gasolina"),
            Pair(gastoServiciosSalud, "Servicios de salud"),
            Pair(gastoEducacion, "Educación"),
            Pair(gastoRecreacion, "Recreación"),
            Pair(gastoComidasFuera, "Comidas fuera"),
            Pair(gastoVestidoCalzado, "Vestido y calzado"),
            Pair(gastoPensionVehiculo, "Pensión vehiculo"),
            Pair(gastoTelefonoCelular, "Teléfono celular"),
            Pair(gastoTelevisionPago, "Televisión de pago"),
            Pair(gastoPagoCreditos, "Pago créditos")
        )

        // Validar que los montos sean numéricos
        for ((campo, nombre) in gastosMonetarios) {
            if (campo.text.toString().isNotBlank() && campo.text.toString().toDoubleOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El monto de '$nombre' debe ser numérico",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar que si hay monto, debe haber motivo
        for ((campoMonto, campoMotivo, nombre) in listOf(
            Triple(gastoDespensaAlimentacion, gastoDespensaMotivo, "despensa y alimentación"),
            Triple(gastoGas, gastoGasMotivo, "gas"),
            Triple(gastoLuz, gastoLuzMotivo, "luz"),
            Triple(gastoAgua, gastoAguaMotivo, "agua"),
            Triple(gastoServicioTelefonico, gastoServicioTelefonicoMotivo, "servicio telefónico"),
            Triple(gastoMantenimientoVivienda, gastoMantenimientoMotivo, "mantenimiento vivienda"),
            Triple(gastoTransportePublico, gastoTransporteMotivo, "transporte público"),
            Triple(gastoGasolina, gastoGasolinaMotivo, "gasolina"),
            Triple(gastoServiciosSalud, gastoSaludMotivo, "servicios de salud"),
            Triple(gastoEducacion, gastoEducacionMotivo, "educación"),
            Triple(gastoRecreacion, gastoRecreacionMotivo, "recreación"),
            Triple(gastoComidasFuera, gastoComidasFueraMotivo, "comidas fuera"),
            Triple(gastoVestidoCalzado, gastoVestidoCalzadoMotivo, "vestido y calzado"),
            Triple(gastoPensionVehiculo, gastoPensionVehiculoMotivo, "pensión vehiculo"),
            Triple(gastoTelefonoCelular, gastoTelefonoCelularMotivo, "teléfono celular"),
            Triple(gastoTelevisionPago, gastoTelevisionPagoMotivo, "televisión de pago"),
            Triple(gastoPagoCreditos, gastoPagoCreditosMotivo, "pago créditos")
        )) {
            if (campoMonto.text.toString().isNotBlank() && campoMotivo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Motivo requerido",
                    "Debe especificar el motivo del gasto en $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campoMotivo.requestFocus()
                return false
            }
        }

        // Validar método de pago
        if (gastoMetodoPago.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar el método de pago principal",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            gastoMetodoPago.requestFocus()
            return false
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
        val datos = datosGastos(
            gasto_despensa_alimentacion = gastoDespensaAlimentacion.text.toString(),
            gasto_despensa_motivo = gastoDespensaMotivo.text.toString(),
            gasto_gas = gastoGas.text.toString(),
            gasto_gas_motivo = gastoGasMotivo.text.toString(),
            gasto_luz = gastoLuz.text.toString(),
            gasto_luz_motivo = gastoLuzMotivo.text.toString(),
            gasto_agua = gastoAgua.text.toString(),
            gasto_agua_motivo = gastoAguaMotivo.text.toString(),
            gasto_servicio_telefonico = gastoServicioTelefonico.text.toString(),
            gasto_servicio_telefonico_motivo = gastoServicioTelefonicoMotivo.text.toString(),
            gasto_mantenimiento_vivienda = gastoMantenimientoVivienda.text.toString(),
            gasto_mantenimiento_motivo = gastoMantenimientoMotivo.text.toString(),
            gasto_transporte_publico = gastoTransportePublico.text.toString(),
            gasto_transporte_motivo = gastoTransporteMotivo.text.toString(),
            gasto_gasolina = gastoGasolina.text.toString(),
            gasto_gasolina_motivo = gastoGasolinaMotivo.text.toString(),
            gasto_servicios_salud = gastoServiciosSalud.text.toString(),
            gasto_salud_motivo = gastoSaludMotivo.text.toString(),
            gasto_educacion = gastoEducacion.text.toString(),
            gasto_educacion_motivo = gastoEducacionMotivo.text.toString(),
            gasto_recreacion = gastoRecreacion.text.toString(),
            gasto_recreacion_motivo = gastoRecreacionMotivo.text.toString(),
            gasto_comidas_fuera = gastoComidasFuera.text.toString(),
            gasto_comidas_fuera_motivo = gastoComidasFueraMotivo.text.toString(),
            gasto_vestido_calzado = gastoVestidoCalzado.text.toString(),
            gasto_vestido_calzado_motivo = gastoVestidoCalzadoMotivo.text.toString(),
            gasto_pension_vehiculo = gastoPensionVehiculo.text.toString(),
            gasto_pension_vehiculo_motivo = gastoPensionVehiculoMotivo.text.toString(),
            gasto_telefono_celular = gastoTelefonoCelular.text.toString(),
            gasto_telefono_celular_motivo = gastoTelefonoCelularMotivo.text.toString(),
            gasto_television_pago = gastoTelevisionPago.text.toString(),
            gasto_television_pago_motivo = gastoTelevisionPagoMotivo.text.toString(),
            gasto_pago_creditos = gastoPagoCreditos.text.toString(),
            gasto_pago_creditos_motivo = gastoPagoCreditosMotivo.text.toString(),
            gasto_otros_descripcion = gastoOtrosDescripcion.text.toString(),
            gasto_otros_motivo = gastoOtrosMotivo.text.toString(),
            gasto_metodo_pago = gastoMetodoPago.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosGastos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos de gastos guardados correctamente",
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
        val intent = Intent(this, Formatoparte12Activity::class.java)
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