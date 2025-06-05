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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte8)

        idAcreditado = intent.getStringExtra("id_acreditado")


        // Vincular vistas
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

        btnGuardar.setOnClickListener {
            guardarDatos()
        }

        btnSiguiente.setOnClickListener {
            irSiguiente()
        }
    }

    private fun guardarDatos() {
        val datos = datosSolicitante(
            hogar_integrantes_trabajando = hogarIntegrantesTrabajando.text.toString(),
            solicitante_activo = solicitanteActivo.text.toString(),
            solicitante_ocupacion_actual = solicitanteOcupacionActual.text.toString(),
            solicitante_desempleado_tiempo = solicitanteDesempleadoTiempo.text.toString(),
            solicitante_empresa_previa = solicitanteEmpresaPrevia.text.toString(),
            solicitante_antiguedad_empleo_anterior = solicitanteAntiguedadEmpleoAnterior.text.toString(),
            institucion_trabajo_solicitante = institucionTrabajoSolicitante.text.toString(),
            actividad_remunerada_solicitante = actividadRemuneradaSolicitante.text.toString(),
            contrato_laboral_solicitante = contratoLaboralSolicitante.text.toString(),
            solicitante_ingreso_mensual = solicitanteIngresoMensual.text.toString(),
            solicitante_empresa = solicitanteEmpresa.text.toString(),
            solicitante_antiguedad = solicitanteAntiguedad.text.toString(),
            comprobante_ingresos_solicitante = comprobanteIngresosSolicitante.text.toString(),
            institucion_cotizacion_solicitante = institucionCotizacionSolicitante.text.toString(),
            ingreso_conceptos_solicitante = ingresoConceptosSolicitante.text.toString(),
            id_acreditado = idAcreditado!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosSolicitante(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte8Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte8Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte8Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte9Activity::class.java) // Ajusta el siguiente Activity real
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
