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

class Formatoparte9Activity : AppCompatActivity() {

    private lateinit var conyugeActivo: EditText
    private lateinit var conyugeOcupacionActual: EditText
    private lateinit var institucionTrabajoConyuge: EditText
    private lateinit var conyugeActividadRemunerada: EditText
    private lateinit var conyugeContratoLaboral: EditText
    private lateinit var conyugeIngresoMensual: EditText
    private lateinit var conyugeEmpresa: EditText
    private lateinit var conyugeAntiguedad: EditText
    private lateinit var comprobanteIngresoConyuge: EditText
    private lateinit var institucionCotizacionConyuge: EditText
    private lateinit var ingresosConceptosConyuge: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte9)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Vincular vistas
        conyugeActivo = findViewById(R.id.conyuge_activo)
        conyugeOcupacionActual = findViewById(R.id.conyuge_ocupacion_actual)
        institucionTrabajoConyuge = findViewById(R.id.institucion_trabajo_conyuge)
        conyugeActividadRemunerada = findViewById(R.id.conyuge_actividad_remunerada)
        conyugeContratoLaboral = findViewById(R.id.conyuge_contrato_laboral)
        conyugeIngresoMensual = findViewById(R.id.conyuge_ingreso_mensual)
        conyugeEmpresa = findViewById(R.id.conyuge_empresa)
        conyugeAntiguedad = findViewById(R.id.conyuge_antiguedad)
        comprobanteIngresoConyuge = findViewById(R.id.comprobante_ingreso_conyuge)
        institucionCotizacionConyuge = findViewById(R.id.institucion_cotizacion_conyuge)
        ingresosConceptosConyuge = findViewById(R.id.ingresos_conceptos_conyuge)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatos()
        }

        btnSiguiente.setOnClickListener {
            irSiguiente()
        }
    }

    private fun guardarDatos() {
        val datos = datosEspecificosConyuge(
            conyuge_activo = conyugeActivo.text.toString(),
            conyuge_ocupacion_actual = conyugeOcupacionActual.text.toString(),
            institucion_trabajo_conyuge = institucionTrabajoConyuge.text.toString(),
            conyuge_actividad_remunerada = conyugeActividadRemunerada.text.toString(),
            conyuge_contrato_laboral = conyugeContratoLaboral.text.toString(),
            conyuge_ingreso_mensual = conyugeIngresoMensual.text.toString(),
            conyuge_empresa = conyugeEmpresa.text.toString(),
            conyuge_antiguedad = conyugeAntiguedad.text.toString(),
            comprobante_ingreso_conyuge = comprobanteIngresoConyuge.text.toString(),
            institucion_cotizacion_conyuge = institucionCotizacionConyuge.text.toString(),
            ingresos_conceptos_conyuge = ingresosConceptosConyuge.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        // Ejemplo con Retrofit y corrutinas
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosEspecificosConyuge(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte9Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte9Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte9Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        // Ajusta el siguiente Activity según corresponda
        val intent = Intent(this, Formatoparte10Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
