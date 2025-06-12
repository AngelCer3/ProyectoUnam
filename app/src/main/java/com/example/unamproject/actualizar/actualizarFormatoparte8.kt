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
import com.example.unamproject.datosSolicitante
import kotlinx.coroutines.launch

class actualizarFormatoparte8 : AppCompatActivity() {

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

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte8)

        // Vincular EditText con IDs del layout
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

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosSolicitante()

        btnActualizarDatos.setOnClickListener {
            actualizarDatosSolicitante()
        }

        btnSiguiente.setOnClickListener {
            irASiguiente()
        }
    }

    private fun cargarDatosSolicitante() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosSolicitante(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    hogarIntegrantesTrabajando.setText(datos.hogar_integrantes_trabajando)
                    solicitanteActivo.setText(datos.solicitante_activo)
                    solicitanteOcupacionActual.setText(datos.solicitante_ocupacion_actual)
                    solicitanteDesempleadoTiempo.setText(datos.solicitante_desempleado_tiempo)
                    solicitanteEmpresaPrevia.setText(datos.solicitante_empresa_previa)
                    solicitanteAntiguedadEmpleoAnterior.setText(datos.solicitante_antiguedad_trabajo_anterior)
                    institucionTrabajoSolicitante.setText(datos.institucion_trabajo_solicitante)
                    actividadRemuneradaSolicitante.setText(datos.actividad_remunerada_solicitante)
                    contratoLaboralSolicitante.setText(datos.contrato_laboral_solicitante)
                    solicitanteIngresoMensual.setText(datos.solicitante_ingreso_mensual)
                    solicitanteEmpresa.setText(datos.solicitante_empresa)
                    solicitanteAntiguedad.setText(datos.solicitante_antiguedad)
                    comprobanteIngresosSolicitante.setText(datos.comprobante_ingresos_solicitante)
                    institucionCotizacionSolicitante.setText(datos.institucion_cotizacion_solicitante)
                    ingresoConceptosSolicitante.setText(datos.ingresos_conceptos_solicitante)
                } else {
                    Toast.makeText(this@actualizarFormatoparte8, "No se encontraron datos del solicitante", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte8, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosSolicitante() {
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

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosSolicitante(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte8, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte8, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte8, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        // Cambiar siguienteActivity por la actividad correspondiente
        val intent = Intent(this, actualizarFormatoparte9::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
