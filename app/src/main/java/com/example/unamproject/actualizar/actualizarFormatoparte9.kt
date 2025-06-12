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
import com.example.unamproject.datosEspecificosConyuge
import kotlinx.coroutines.launch

class actualizarFormatoparte9 : AppCompatActivity() {

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

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte9)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vincular EditText con IDs del layout
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

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosConyuge()

        btnActualizarDatos.setOnClickListener {
            actualizarDatosConyuge()
        }

        btnSiguiente.setOnClickListener {
            irASiguiente()
        }
    }

    private fun cargarDatosConyuge() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosEspecificosConyuge(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    conyugeActivo.setText(datos.conyuge_activo)
                    conyugeOcupacionActual.setText(datos.conyuge_ocupacion_actual)
                    institucionTrabajoConyuge.setText(datos.institucion_trabajo_conyuge)
                    conyugeActividadRemunerada.setText(datos.conyuge_actividad_remunerada)
                    conyugeContratoLaboral.setText(datos.conyuge_contrato_laboral)
                    conyugeIngresoMensual.setText(datos.conyuge_ingreso_mensual)
                    conyugeEmpresa.setText(datos.conyuge_empresa)
                    conyugeAntiguedad.setText(datos.conyuge_antiguedad)
                    comprobanteIngresoConyuge.setText(datos.comprobante_ingreso_conyuge)
                    institucionCotizacionConyuge.setText(datos.institucion_cotizacion_conyuge)
                    ingresosConceptosConyuge.setText(datos.ingresos_conceptos_conyuge)
                } else {
                    Toast.makeText(this@actualizarFormatoparte9, "No se encontraron datos del cónyuge", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte9, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosConyuge() {
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
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosEspecificosConyuge(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte9, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte9, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte9, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        // Cambiar siguienteActivity por la actividad que sigue
        val intent = Intent(this, actualizarFormatoparte10::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
