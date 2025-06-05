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

class Formatoparte5SinConexion : AppCompatActivity() {

    // Declara todos los EditText según los campos de DatosReestructuraEntity
    private lateinit var motivoEt: EditText
    private lateinit var documentoEt: EditText
    private lateinit var tipoDocumentoEt: EditText
    private lateinit var solicitanteEsAcredEt: EditText
    private lateinit var solicitanteNombreEt: EditText
    private lateinit var parentescoEt: EditText
    private lateinit var motivoPersonalEt: EditText
    private lateinit var sexoEt: EditText
    private lateinit var fechaNacimientoEt: EditText
    private lateinit var edadEt: EditText
    private lateinit var lugarNacimientoEt: EditText
    private lateinit var gradoEstudiosEt: EditText
    private lateinit var conocimientoCompEt: EditText
    private lateinit var discapacidadEt: EditText
    private lateinit var dictamenEt: EditText
    private lateinit var institucionDictamenEt: EditText
    private lateinit var fechaDictamenEt: EditText
    private lateinit var porcentajeDiscapacidadEt: EditText
    private lateinit var estadoCivilEt: EditText
    private lateinit var fechaEstadoCivilEt: EditText
    private lateinit var exesposoAportacionEt: EditText
    private lateinit var exesposoMontoEt: EditText
    private lateinit var regimenConyugalEt: EditText
    private lateinit var viveConConyugeEt: EditText
    private lateinit var fechaNoConviveEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte5_sin_conexion)

        // Obtener id_acreditado del Intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""

        // Inicializar base de datos Room
        database = AppDatabase.getDatabase(this)

        // Vincular vistas
        motivoEt = findViewById(R.id.reestructura_motivo)
        documentoEt = findViewById(R.id.reestructura_documento)
        tipoDocumentoEt = findViewById(R.id.reestructura_tipo_documento)
        solicitanteEsAcredEt = findViewById(R.id.reestructura_solicitante_es_acred)
        solicitanteNombreEt = findViewById(R.id.reestructura_solicitante_nombre)
        parentescoEt = findViewById(R.id.reestructura_parentesco)
        motivoPersonalEt = findViewById(R.id.reestructura_motivo_personal)
        sexoEt = findViewById(R.id.reestructura_sexo)
        fechaNacimientoEt = findViewById(R.id.reestructura_fecha_nacimiento)
        edadEt = findViewById(R.id.reestructura_edad)
        lugarNacimientoEt = findViewById(R.id.reestructura_lugar_nacimiento)
        gradoEstudiosEt = findViewById(R.id.reestructura_grado_estudios)
        conocimientoCompEt = findViewById(R.id.reestructura_conocimiento_comp)
        discapacidadEt = findViewById(R.id.reestructura_discapacidad)
        dictamenEt = findViewById(R.id.reestructura_dictamen)
        institucionDictamenEt = findViewById(R.id.reestructura_institucion_dictamen)
        fechaDictamenEt = findViewById(R.id.reestructura_fecha_dictamen)
        porcentajeDiscapacidadEt = findViewById(R.id.reestructura_porcentaje_discapacidad)
        estadoCivilEt = findViewById(R.id.reestructura_estado_civil)
        fechaEstadoCivilEt = findViewById(R.id.reestructura_fecha_estado_civil)
        exesposoAportacionEt = findViewById(R.id.reestructura_exesposo_aportacion)
        exesposoMontoEt = findViewById(R.id.reestructura_exesposo_monto)
        regimenConyugalEt = findViewById(R.id.reestructura_regimen_conyugal)
        viveConConyugeEt = findViewById(R.id.reestructura_vive_con_conyuge)
        fechaNoConviveEt = findViewById(R.id.reestructura_fecha_no_convive)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosReestructura()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarDatosReestructura() {
        val datos = DatosReestructuraEntity(
            reestructura_motivo = motivoEt.text.toString(),
            reestructura_documento = documentoEt.text.toString(),
            reestructura_tipo_documento = tipoDocumentoEt.text.toString(),
            reestructura_solicitante_es_acred = solicitanteEsAcredEt.text.toString(),
            reestructura_solicitante_nombre = solicitanteNombreEt.text.toString(),
            reestructura_parentesco = parentescoEt.text.toString(),
            reestructura_motivo_personal = motivoPersonalEt.text.toString(),
            reestructura_sexo = sexoEt.text.toString(),
            reestructura_fecha_nacimiento = fechaNacimientoEt.text.toString(),
            reestructura_edad = edadEt.text.toString(),
            reestructura_lugar_nacimiento = lugarNacimientoEt.text.toString(),
            reestructura_grado_estudios = gradoEstudiosEt.text.toString(),
            reestructura_conocimiento_comp = conocimientoCompEt.text.toString(),
            reestructura_discapacidad = discapacidadEt.text.toString(),
            reestructura_dictamen = dictamenEt.text.toString(),
            reestructura_institucion_dictamen = institucionDictamenEt.text.toString(),
            reestructura_fecha_dictamen = fechaDictamenEt.text.toString(),
            reestructura_porcentaje_discapacidad = porcentajeDiscapacidadEt.text.toString(),
            reestructura_estado_civil = estadoCivilEt.text.toString(),
            reestructura_fecha_estado_civil = fechaEstadoCivilEt.text.toString(),
            reestructura_exesposo_aportacion = exesposoAportacionEt.text.toString(),
            reestructura_exesposo_monto = exesposoMontoEt.text.toString(),
            reestructura_regimen_conyugal = regimenConyugalEt.text.toString(),
            reestructura_vive_con_conyuge = viveConConyugeEt.text.toString(),
            reestructura_fecha_no_convive = fechaNoConviveEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosReestructuraDao().insertDatosReestructura(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte5SinConexion, "Datos guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte5SinConexion, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte6SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
