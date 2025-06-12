package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte9SinConexion : AppCompatActivity() {

    private lateinit var conyugeActivoEt: EditText
    private lateinit var ocupacionActualEt: EditText
    private lateinit var institucionTrabajoEt: EditText
    private lateinit var actividadRemuneradaEt: EditText
    private lateinit var contratoLaboralEt: EditText
    private lateinit var ingresoMensualEt: EditText
    private lateinit var empresaEt: EditText
    private lateinit var antiguedadEt: EditText
    private lateinit var comprobanteIngresoEt: EditText
    private lateinit var institucionCotizacionEt: EditText
    private lateinit var conceptosIngresoEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte9_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        conyugeActivoEt = findViewById(R.id.conyuge_activo)
        ocupacionActualEt = findViewById(R.id.conyuge_ocupacion_actual)
        institucionTrabajoEt = findViewById(R.id.institucion_trabajo_conyuge)
        actividadRemuneradaEt = findViewById(R.id.conyuge_actividad_remunerada)
        contratoLaboralEt = findViewById(R.id.conyuge_contrato_laboral)
        ingresoMensualEt = findViewById(R.id.conyuge_ingreso_mensual)
        empresaEt = findViewById(R.id.conyuge_empresa)
        antiguedadEt = findViewById(R.id.conyuge_antiguedad)
        comprobanteIngresoEt = findViewById(R.id.comprobante_ingreso_conyuge)
        institucionCotizacionEt = findViewById(R.id.institucion_cotizacion_conyuge)
        conceptosIngresoEt = findViewById(R.id.ingresos_conceptos_conyuge)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosConyuge()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarDatosConyuge() {
        val datos = DatosEspecificosConyugeEntity(
            conyuge_activo = conyugeActivoEt.text.toString(),
            conyuge_ocupacion_actual = ocupacionActualEt.text.toString(),
            institucion_trabajo_conyuge = institucionTrabajoEt.text.toString(),
            conyuge_actividad_remunerada = actividadRemuneradaEt.text.toString(),
            conyuge_contrato_laboral = contratoLaboralEt.text.toString(),
            conyuge_ingreso_mensual = ingresoMensualEt.text.toString(),
            conyuge_empresa = empresaEt.text.toString(),
            conyuge_antiguedad = antiguedadEt.text.toString(),
            comprobante_ingreso_conyuge = comprobanteIngresoEt.text.toString(),
            institucion_cotizacion_conyuge = institucionCotizacionEt.text.toString(),
            ingresos_conceptos_conyuge = conceptosIngresoEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosEspecificosConyugeDao().insertDatosEspecificosConyuge(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte9SinConexion, "Datos del cónyuge guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte9SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte10SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
