package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosGastos
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte11 : AppCompatActivity() {

    // Declaración de todas las vistas
    private lateinit var despensaAlimentacion: EditText
    private lateinit var despensaMotivo: EditText
    private lateinit var gas: EditText
    private lateinit var gasMotivo: EditText
    private lateinit var luz: EditText
    private lateinit var luzMotivo: EditText
    private lateinit var agua: EditText
    private lateinit var aguaMotivo: EditText
    private lateinit var servicioTelefonico: EditText
    private lateinit var servicioTelefonicoMotivo: EditText
    private lateinit var mantenimientoVivienda: EditText
    private lateinit var mantenimientoMotivo: EditText
    private lateinit var transportePublico: EditText
    private lateinit var transporteMotivo: EditText
    private lateinit var gasolina: EditText
    private lateinit var gasolinaMotivo: EditText
    private lateinit var serviciosSalud: EditText
    private lateinit var saludMotivo: EditText
    private lateinit var educacion: EditText
    private lateinit var educacionMotivo: EditText
    private lateinit var recreacion: EditText
    private lateinit var recreacionMotivo: EditText
    private lateinit var comidasFuera: EditText
    private lateinit var comidasFueraMotivo: EditText
    private lateinit var vestidoCalzado: EditText
    private lateinit var vestidoCalzadoMotivo: EditText
    private lateinit var pensionVehiculo: EditText
    private lateinit var pensionVehiculoMotivo: EditText
    private lateinit var telefonoCelular: EditText
    private lateinit var telefonoCelularMotivo: EditText
    private lateinit var televisionPago: EditText
    private lateinit var televisionPagoMotivo: EditText
    private lateinit var pagoCreditos: EditText
    private lateinit var pagoCreditosMotivo: EditText
    private lateinit var otrosDescripcion: EditText
    private lateinit var otrosMotivo: EditText
    private lateinit var metodoPago: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte11)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupButtons()

        // Validar idAcreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        cargarDatosGastos()
    }

    private fun initViews() {
        // Inicialización de todas las vistas
        despensaAlimentacion = findViewById(R.id.gasto_despensa_alimentacion)
        despensaMotivo = findViewById(R.id.gasto_despensa_motivo)
        gas = findViewById(R.id.gasto_gas)
        gasMotivo = findViewById(R.id.gasto_gas_motivo)
        luz = findViewById(R.id.gasto_luz)
        luzMotivo = findViewById(R.id.gasto_luz_motivo)
        agua = findViewById(R.id.gasto_agua)
        aguaMotivo = findViewById(R.id.gasto_agua_motivo)
        servicioTelefonico = findViewById(R.id.gasto_servicio_telefonico)
        servicioTelefonicoMotivo = findViewById(R.id.gasto_servicio_telefonico_motivo)
        mantenimientoVivienda = findViewById(R.id.gasto_mantenimiento_vivienda)
        mantenimientoMotivo = findViewById(R.id.gasto_mantenimiento_motivo)
        transportePublico = findViewById(R.id.gasto_transporte_publico)
        transporteMotivo = findViewById(R.id.gasto_transporte_motivo)
        gasolina = findViewById(R.id.gasto_gasolina)
        gasolinaMotivo = findViewById(R.id.gasto_gasolina_motivo)
        serviciosSalud = findViewById(R.id.gasto_servicios_salud)
        saludMotivo = findViewById(R.id.gasto_salud_motivo)
        educacion = findViewById(R.id.gasto_educacion)
        educacionMotivo = findViewById(R.id.gasto_educacion_motivo)
        recreacion = findViewById(R.id.gasto_recreacion)
        recreacionMotivo = findViewById(R.id.gasto_recreacion_motivo)
        comidasFuera = findViewById(R.id.gasto_comidas_fuera)
        comidasFueraMotivo = findViewById(R.id.gasto_comidas_fuera_motivo)
        vestidoCalzado = findViewById(R.id.gasto_vestido_calzado)
        vestidoCalzadoMotivo = findViewById(R.id.gasto_vestido_calzado_motivo)
        pensionVehiculo = findViewById(R.id.gasto_pension_vehiculo)
        pensionVehiculoMotivo = findViewById(R.id.gasto_pension_vehiculo_motivo)
        telefonoCelular = findViewById(R.id.gasto_telefono_celular)
        telefonoCelularMotivo = findViewById(R.id.gasto_telefono_celular_motivo)
        televisionPago = findViewById(R.id.gasto_television_pago)
        televisionPagoMotivo = findViewById(R.id.gasto_television_pago_motivo)
        pagoCreditos = findViewById(R.id.gasto_pago_creditos)
        pagoCreditosMotivo = findViewById(R.id.gasto_pago_creditos_motivo)
        otrosDescripcion = findViewById(R.id.gasto_otros_descripcion)
        otrosMotivo = findViewById(R.id.gasto_otros_motivo)
        metodoPago = findViewById(R.id.gasto_metodo_pago)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
            if (validarFormulario()) {
                mostrarConfirmacionGuardado()
            }
        }

        btnSiguiente.setOnClickListener {
            if (registroExistente) {
                irASiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarDatos() }
                )
            }
        }
    }

    private fun cargarDatosGastos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosGastos(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        // Llenar todos los campos con los datos recibidos
                        despensaAlimentacion.setText(datos.gasto_despensa_alimentacion)
                        despensaMotivo.setText(datos.gasto_despensa_motivo)
                        gas.setText(datos.gasto_gas)
                        gasMotivo.setText(datos.gasto_gas_motivo)
                        luz.setText(datos.gasto_luz)
                        luzMotivo.setText(datos.gasto_luz_motivo)
                        agua.setText(datos.gasto_agua)
                        aguaMotivo.setText(datos.gasto_agua_motivo)
                        servicioTelefonico.setText(datos.gasto_servicio_telefonico)
                        servicioTelefonicoMotivo.setText(datos.gasto_servicio_telefonico_motivo)
                        mantenimientoVivienda.setText(datos.gasto_mantenimiento_vivienda)
                        mantenimientoMotivo.setText(datos.gasto_mantenimiento_motivo)
                        transportePublico.setText(datos.gasto_transporte_publico)
                        transporteMotivo.setText(datos.gasto_transporte_motivo)
                        gasolina.setText(datos.gasto_gasolina)
                        gasolinaMotivo.setText(datos.gasto_gasolina_motivo)
                        serviciosSalud.setText(datos.gasto_servicios_salud)
                        saludMotivo.setText(datos.gasto_salud_motivo)
                        educacion.setText(datos.gasto_educacion)
                        educacionMotivo.setText(datos.gasto_educacion_motivo)
                        recreacion.setText(datos.gasto_recreacion)
                        recreacionMotivo.setText(datos.gasto_recreacion_motivo)
                        comidasFuera.setText(datos.gasto_comidas_fuera)
                        comidasFueraMotivo.setText(datos.gasto_comidas_fuera_motivo)
                        vestidoCalzado.setText(datos.gasto_vestido_calzado)
                        vestidoCalzadoMotivo.setText(datos.gasto_vestido_calzado_motivo)
                        pensionVehiculo.setText(datos.gasto_pension_vehiculo)
                        pensionVehiculoMotivo.setText(datos.gasto_pension_vehiculo_motivo)
                        telefonoCelular.setText(datos.gasto_telefono_celular)
                        telefonoCelularMotivo.setText(datos.gasto_telefono_celular_motivo)
                        televisionPago.setText(datos.gasto_television_pago)
                        televisionPagoMotivo.setText(datos.gasto_television_pago_motivo)
                        pagoCreditos.setText(datos.gasto_pago_creditos)
                        pagoCreditosMotivo.setText(datos.gasto_pago_creditos_motivo)
                        otrosDescripcion.setText(datos.gasto_otros_descripcion)
                        otrosMotivo.setText(datos.gasto_otros_motivo)
                        metodoPago.setText(datos.gasto_metodo_pago)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de gastos. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun validarFormulario(): Boolean {
        // Validar campos monetarios (deben ser números válidos)
        val camposMonetarios = listOf(
            despensaAlimentacion to "Despensa y alimentación",
            gas to "Gas",
            luz to "Luz",
            agua to "Agua",
            servicioTelefonico to "Servicio telefónico",
            mantenimientoVivienda to "Mantenimiento vivienda",
            transportePublico to "Transporte público",
            gasolina to "Gasolina",
            serviciosSalud to "Servicios de salud",
            educacion to "Educación",
            recreacion to "Recreación",
            comidasFuera to "Comidas fuera",
            vestidoCalzado to "Vestido y calzado",
            pensionVehiculo to "Pensión vehículo",
            telefonoCelular to "Teléfono celular",
            televisionPago to "Televisión de pago",
            pagoCreditos to "Pago de créditos"
        )

        for ((campo, nombre) in camposMonetarios) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty() && !valor.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
                campo.error = "Formato inválido"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "El campo $nombre debe ser un valor monetario válido (ej. 1250 o 1250.50)",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar que si hay un monto, debe haber un motivo
        val gruposCampos = listOf(
            Pair(despensaAlimentacion, despensaMotivo) to "Despensa y alimentación",
            Pair(gas, gasMotivo) to "Gas",
            Pair(luz, luzMotivo) to "Luz",
            Pair(agua, aguaMotivo) to "Agua",
            Pair(servicioTelefonico, servicioTelefonicoMotivo) to "Servicio telefónico",
            Pair(mantenimientoVivienda, mantenimientoMotivo) to "Mantenimiento vivienda",
            Pair(transportePublico, transporteMotivo) to "Transporte público",
            Pair(gasolina, gasolinaMotivo) to "Gasolina",
            Pair(serviciosSalud, saludMotivo) to "Servicios de salud",
            Pair(educacion, educacionMotivo) to "Educación",
            Pair(recreacion, recreacionMotivo) to "Recreación",
            Pair(comidasFuera, comidasFueraMotivo) to "Comidas fuera",
            Pair(vestidoCalzado, vestidoCalzadoMotivo) to "Vestido y calzado",
            Pair(pensionVehiculo, pensionVehiculoMotivo) to "Pensión vehículo",
            Pair(telefonoCelular, telefonoCelularMotivo) to "Teléfono celular",
            Pair(televisionPago, televisionPagoMotivo) to "Televisión de pago",
            Pair(pagoCreditos, pagoCreditosMotivo) to "Pago de créditos",
            Pair(otrosDescripcion, otrosMotivo) to "Otros gastos"
        )

        for ((grupo, nombreGrupo) in gruposCampos) {
            val (monto, motivo) = grupo
            if (monto.text.toString().isNotEmpty() && motivo.text.toString().isEmpty()) {
                motivo.error = "Debe especificar el motivo"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "Si especifica un monto para $nombreGrupo, debe indicar el motivo",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar método de pago si hay algún gasto registrado
        val hayGastos = camposMonetarios.any { (campo, _) -> campo.text.toString().isNotEmpty() }
        if (hayGastos && metodoPago.text.toString().isEmpty()) {
            metodoPago.error = "Requerido"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Debe especificar el método de pago principal",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar los datos de gastos?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatos() }
        )
    }

    private fun guardarOActualizarDatos() {
        val datos = datosGastos(
            gasto_despensa_alimentacion = despensaAlimentacion.text.toString().trim(),
            gasto_despensa_motivo = despensaMotivo.text.toString().trim(),
            gasto_gas = gas.text.toString().trim(),
            gasto_gas_motivo = gasMotivo.text.toString().trim(),
            gasto_luz = luz.text.toString().trim(),
            gasto_luz_motivo = luzMotivo.text.toString().trim(),
            gasto_agua = agua.text.toString().trim(),
            gasto_agua_motivo = aguaMotivo.text.toString().trim(),
            gasto_servicio_telefonico = servicioTelefonico.text.toString().trim(),
            gasto_servicio_telefonico_motivo = servicioTelefonicoMotivo.text.toString().trim(),
            gasto_mantenimiento_vivienda = mantenimientoVivienda.text.toString().trim(),
            gasto_mantenimiento_motivo = mantenimientoMotivo.text.toString().trim(),
            gasto_transporte_publico = transportePublico.text.toString().trim(),
            gasto_transporte_motivo = transporteMotivo.text.toString().trim(),
            gasto_gasolina = gasolina.text.toString().trim(),
            gasto_gasolina_motivo = gasolinaMotivo.text.toString().trim(),
            gasto_servicios_salud = serviciosSalud.text.toString().trim(),
            gasto_salud_motivo = saludMotivo.text.toString().trim(),
            gasto_educacion = educacion.text.toString().trim(),
            gasto_educacion_motivo = educacionMotivo.text.toString().trim(),
            gasto_recreacion = recreacion.text.toString().trim(),
            gasto_recreacion_motivo = recreacionMotivo.text.toString().trim(),
            gasto_comidas_fuera = comidasFuera.text.toString().trim(),
            gasto_comidas_fuera_motivo = comidasFueraMotivo.text.toString().trim(),
            gasto_vestido_calzado = vestidoCalzado.text.toString().trim(),
            gasto_vestido_calzado_motivo = vestidoCalzadoMotivo.text.toString().trim(),
            gasto_pension_vehiculo = pensionVehiculo.text.toString().trim(),
            gasto_pension_vehiculo_motivo = pensionVehiculoMotivo.text.toString().trim(),
            gasto_telefono_celular = telefonoCelular.text.toString().trim(),
            gasto_telefono_celular_motivo = telefonoCelularMotivo.text.toString().trim(),
            gasto_television_pago = televisionPago.text.toString().trim(),
            gasto_television_pago_motivo = televisionPagoMotivo.text.toString().trim(),
            gasto_pago_creditos = pagoCreditos.text.toString().trim(),
            gasto_pago_creditos_motivo = pagoCreditosMotivo.text.toString().trim(),
            gasto_otros_descripcion = otrosDescripcion.text.toString().trim(),
            gasto_otros_motivo = otrosMotivo.text.toString().trim(),
            gasto_metodo_pago = metodoPago.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosGastos(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosGastos(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos de gastos actualizados correctamente"
                                else
                                    "Datos de gastos guardados correctamente",
                                iconoResId = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF388E3C.toInt()
                            )
                        } else {
                            mostrarErrorServidor("El servidor no pudo procesar la solicitud")
                        }
                    } ?: mostrarErrorServidor("Respuesta vacía del servidor")
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun manejarErrorRespuesta(codigo: Int, mensajeError: String?) {
        when (codigo) {
            404 -> mostrarDialogo(
                titulo = "No encontrado",
                mensaje = "El registro no fue encontrado",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            400 -> mostrarDialogo(
                titulo = "Datos inválidos",
                mensaje = "Verifique la información ingresada: ${mensajeError ?: "Error 400"}",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            500 -> mostrarErrorServidor("Error interno del servidor: ${mensajeError ?: "sin detalles"}")
            else -> mostrarErrorServidor("Error $codigo: ${mensajeError ?: "Error desconocido"}")
        }
    }

    private fun mostrarErrorServidor(mensaje: String) {
        mostrarDialogo(
            titulo = "Error del servidor",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorConexion(mensaje: String) {
        mostrarDialogo(
            titulo = "Error de conexión",
            mensaje = "No se pudo conectar al servidor: $mensaje",
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = "Ocurrió un error: $mensaje",
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

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte12::class.java).apply {
            putExtra("id_acreditado", idAcreditado)
            putExtra("id_usuario", idUsuario)
        }
        startActivity(intent)
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