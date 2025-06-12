package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosGastos
import kotlinx.coroutines.launch

class actualizarFormatoparte11 : AppCompatActivity() {

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

    private lateinit var botonGuardar: Button
    private lateinit var botonSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte11)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener id_acreditado del Intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")

        // Referencias a los EditText
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

        botonGuardar = findViewById(R.id.btnActualizar)
        botonSiguiente = findViewById(R.id.btnSiguiente)

        botonGuardar.setOnClickListener {
            guardarDatos()
        }

        botonSiguiente.setOnClickListener {
            irASiguiente()
        }

        cargarDatos()
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosGastos(idAcreditado)
                if (response.isSuccessful) {
                    response.body()?.let { datos ->
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
                    }
                } else {
                    Toast.makeText(this@actualizarFormatoparte11, "Error al cargar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte11, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun guardarDatos() {
        val datos = datosGastos(
            gasto_despensa_alimentacion = despensaAlimentacion.text.toString(),
            gasto_despensa_motivo = despensaMotivo.text.toString(),
            gasto_gas = gas.text.toString(),
            gasto_gas_motivo = gasMotivo.text.toString(),
            gasto_luz = luz.text.toString(),
            gasto_luz_motivo = luzMotivo.text.toString(),
            gasto_agua = agua.text.toString(),
            gasto_agua_motivo = aguaMotivo.text.toString(),
            gasto_servicio_telefonico = servicioTelefonico.text.toString(),
            gasto_servicio_telefonico_motivo = servicioTelefonicoMotivo.text.toString(),
            gasto_mantenimiento_vivienda = mantenimientoVivienda.text.toString(),
            gasto_mantenimiento_motivo = mantenimientoMotivo.text.toString(),
            gasto_transporte_publico = transportePublico.text.toString(),
            gasto_transporte_motivo = transporteMotivo.text.toString(),
            gasto_gasolina = gasolina.text.toString(),
            gasto_gasolina_motivo = gasolinaMotivo.text.toString(),
            gasto_servicios_salud = serviciosSalud.text.toString(),
            gasto_salud_motivo = saludMotivo.text.toString(),
            gasto_educacion = educacion.text.toString(),
            gasto_educacion_motivo = educacionMotivo.text.toString(),
            gasto_recreacion = recreacion.text.toString(),
            gasto_recreacion_motivo = recreacionMotivo.text.toString(),
            gasto_comidas_fuera = comidasFuera.text.toString(),
            gasto_comidas_fuera_motivo = comidasFueraMotivo.text.toString(),
            gasto_vestido_calzado = vestidoCalzado.text.toString(),
            gasto_vestido_calzado_motivo = vestidoCalzadoMotivo.text.toString(),
            gasto_pension_vehiculo = pensionVehiculo.text.toString(),
            gasto_pension_vehiculo_motivo = pensionVehiculoMotivo.text.toString(),
            gasto_telefono_celular = telefonoCelular.text.toString(),
            gasto_telefono_celular_motivo = telefonoCelularMotivo.text.toString(),
            gasto_television_pago = televisionPago.text.toString(),
            gasto_television_pago_motivo = televisionPagoMotivo.text.toString(),
            gasto_pago_creditos = pagoCreditos.text.toString(),
            gasto_pago_creditos_motivo = pagoCreditosMotivo.text.toString(),
            gasto_otros_descripcion = otrosDescripcion.text.toString(),
            gasto_otros_motivo = otrosMotivo.text.toString(),
            gasto_metodo_pago = metodoPago.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosGastos(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte11, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte11, "Error al guardar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte11, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte12::class.java) // Cambia a la actividad que sigue
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
