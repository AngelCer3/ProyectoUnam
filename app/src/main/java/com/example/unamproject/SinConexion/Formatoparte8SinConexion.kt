package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte8SinConexion : AppCompatActivity() {

    private lateinit var integrantesTrabajandoEt: EditText
    private lateinit var solicitanteActivoEt: EditText
    private lateinit var ocupacionActualEt: EditText
    private lateinit var desempleadoTiempoEt: EditText
    private lateinit var empresaPreviaEt: EditText
    private lateinit var antiguedadTrabajoAnteriorEt: EditText
    private lateinit var institucionTrabajoEt: EditText
    private lateinit var actividadRemuneradaEt: EditText
    private lateinit var contratoLaboralEt: EditText
    private lateinit var ingresoMensualEt: EditText
    private lateinit var empresaSolicitanteEt: EditText
    private lateinit var antiguedadSolicitanteEt: EditText
    private lateinit var comprobanteIngresosEt: EditText
    private lateinit var institucionCotizacionEt: EditText
    private lateinit var ingresosConceptosEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte8_sin_conexion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""

        database = AppDatabase.getDatabase(this)

        // Inicializar EditTexts
        integrantesTrabajandoEt = findViewById(R.id.hogar_integrantes_trabajando)
        solicitanteActivoEt = findViewById(R.id.solicitante_activo)
        ocupacionActualEt = findViewById(R.id.solicitante_ocupacion_actual)
        desempleadoTiempoEt = findViewById(R.id.solicitante_desempleado_tiempo)
        empresaPreviaEt = findViewById(R.id.solicitante_empresa_previa)
        antiguedadTrabajoAnteriorEt = findViewById(R.id.solicitante_antiguedad_empleo_anterior)
        institucionTrabajoEt = findViewById(R.id.institucion_trabajo_solicitante)
        actividadRemuneradaEt = findViewById(R.id.actividad_remunerada_solicitante)
        contratoLaboralEt = findViewById(R.id.contrato_laboral_solicitante)
        ingresoMensualEt = findViewById(R.id.solicitante_ingreso_mensual)
        empresaSolicitanteEt = findViewById(R.id.solicitante_empresa)
        antiguedadSolicitanteEt = findViewById(R.id.solicitante_antiguedad)
        comprobanteIngresosEt = findViewById(R.id.comprobante_ingresos_solicitante)
        institucionCotizacionEt = findViewById(R.id.institucion_cotizacion_solicitante)
        ingresosConceptosEt = findViewById(R.id.ingreso_conceptos_solicitante)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosSolicitante()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarDatosSolicitante() {
        val datos = DatosSolicitanteEntity(
            hogar_integrantes_trabajando = integrantesTrabajandoEt.text.toString(),
            solicitante_activo = solicitanteActivoEt.text.toString(),
            solicitante_ocupacion_actual = ocupacionActualEt.text.toString(),
            solicitante_desempleado_tiempo = desempleadoTiempoEt.text.toString(),
            solicitante_empresa_previa = empresaPreviaEt.text.toString(),
            solicitante_antiguedad_trabajo_anterior = antiguedadTrabajoAnteriorEt.text.toString(),
            institucion_trabajo_solicitante = institucionTrabajoEt.text.toString(),
            actividad_remunerada_solicitante = actividadRemuneradaEt.text.toString(),
            contrato_laboral_solicitante = contratoLaboralEt.text.toString(),
            solicitante_ingreso_mensual = ingresoMensualEt.text.toString(),
            solicitante_empresa = empresaSolicitanteEt.text.toString(),
            solicitante_antiguedad = antiguedadSolicitanteEt.text.toString(),
            comprobante_ingresos_solicitante = comprobanteIngresosEt.text.toString(),
            institucion_cotizacion_solicitante = institucionCotizacionEt.text.toString(),
            ingresos_conceptos_solicitante = ingresosConceptosEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosSolicitanteDao().insertDatosSolicitante(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte8SinConexion, "Datos guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte8SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte9SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
