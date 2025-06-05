package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte11)

        // Obtener el ID del acreditado del Intent
        idAcreditado = intent.getStringExtra("id_acreditado")

        // Inicializar todas las vistas
        inicializarVistas()

        // Configurar listeners de los botones
        btnGuardar.setOnClickListener { guardarDatos() }
        btnSiguiente.setOnClickListener { irSiguiente() }
    }

    private fun inicializarVistas() {
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

    private fun guardarDatos() {
        // Validar que el ID del acreditado no sea nulo
        if (idAcreditado.isNullOrEmpty()) {
            Toast.makeText(this, "Error: No se encontró el ID del acreditado", Toast.LENGTH_LONG).show()
            return
        }

        // Crear el objeto datosGastos con todos los campos
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
            id_acreditado = idAcreditado!!
        )

        // Enviar los datos al servidor en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosGastos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@Formatoparte11Activity,
                            "Datos de gastos guardados correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@Formatoparte11Activity,
                            "Error al guardar los datos: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte11Activity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte12Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
        finish()
    }
}