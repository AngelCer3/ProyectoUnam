package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte5Activity : AppCompatActivity() {

    private lateinit var guardar: Button
    private lateinit var siguiente: Button

    private lateinit var motivo: EditText
    private lateinit var documento: EditText
    private lateinit var tipoDocumento: EditText
    private lateinit var solicitanteEsAcred: EditText
    private lateinit var solicitanteNombre: EditText
    private lateinit var parentesco: EditText
    private lateinit var motivoPersonal: EditText
    private lateinit var sexo: EditText
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var lugarNacimiento: EditText
    private lateinit var gradoEstudios: EditText
    private lateinit var conocimientoComp: EditText
    private lateinit var discapacidad: EditText
    private lateinit var dictamen: EditText
    private lateinit var institucionDictamen: EditText
    private lateinit var fechaDictamen: EditText
    private lateinit var porcentajeDiscapacidad: EditText
    private lateinit var estadoCivil: EditText
    private lateinit var fechaEstadoCivil: EditText
    private lateinit var exesposoAportacion: EditText
    private lateinit var exesposoMonto: EditText
    private lateinit var regimenConyugal: EditText
    private lateinit var viveConConyuge: EditText
    private lateinit var fechaNoConvive: EditText

    private var idAcreditado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte5)

        idAcreditado = intent.getStringExtra("id_acreditado")


        motivo = findViewById(R.id.reestructura_motivo)
        documento = findViewById(R.id.reestructura_documento)
        tipoDocumento = findViewById(R.id.reestructura_tipo_documento)
        solicitanteEsAcred = findViewById(R.id.reestructura_solicitante_es_acred)
        solicitanteNombre = findViewById(R.id.reestructura_solicitante_nombre)
        parentesco = findViewById(R.id.reestructura_parentesco)
        motivoPersonal = findViewById(R.id.reestructura_motivo_personal)
        sexo = findViewById(R.id.reestructura_sexo)
        fechaNacimiento = findViewById(R.id.reestructura_fecha_nacimiento)
        edad = findViewById(R.id.reestructura_edad)
        lugarNacimiento = findViewById(R.id.reestructura_lugar_nacimiento)
        gradoEstudios = findViewById(R.id.reestructura_grado_estudios)
        conocimientoComp = findViewById(R.id.reestructura_conocimiento_comp)
        discapacidad = findViewById(R.id.reestructura_discapacidad)
        dictamen = findViewById(R.id.reestructura_dictamen)
        institucionDictamen = findViewById(R.id.reestructura_institucion_dictamen)
        fechaDictamen = findViewById(R.id.reestructura_fecha_dictamen)
        porcentajeDiscapacidad = findViewById(R.id.reestructura_porcentaje_discapacidad)
        estadoCivil = findViewById(R.id.reestructura_estado_civil)
        fechaEstadoCivil = findViewById(R.id.reestructura_fecha_estado_civil)
        exesposoAportacion = findViewById(R.id.reestructura_exesposo_aportacion)
        exesposoMonto = findViewById(R.id.reestructura_exesposo_monto)
        regimenConyugal = findViewById(R.id.reestructura_regimen_conyugal)
        viveConConyuge = findViewById(R.id.reestructura_vive_con_conyuge)
        fechaNoConvive = findViewById(R.id.reestructura_fecha_no_convive)

        guardar = findViewById(R.id.btnGuardar)
        siguiente = findViewById(R.id.btnSiguiente)

        guardar.setOnClickListener {
            guardarDatos()
        }

        siguiente.setOnClickListener {
            siguienteFormato()
        }
    }

    private fun guardarDatos() {
        val datos = DatosReestructura(
            reestructura_motivo = motivo.text.toString(),
            reestructura_documento = documento.text.toString(),
            reestructura_tipo_documento = tipoDocumento.text.toString(),
            reestructura_solicitante_es_acred = solicitanteEsAcred.text.toString(),
            reestructura_solicitante_nombre = solicitanteNombre.text.toString(),
            reestructura_parentesco = parentesco.text.toString(),
            reestructura_motivo_personal = motivoPersonal.text.toString(),
            reestructura_sexo = sexo.text.toString(),
            reestructura_fecha_nacimiento = fechaNacimiento.text.toString(),
            reestructura_edad = edad.text.toString(),
            reestructura_lugar_nacimiento = lugarNacimiento.text.toString(),
            reestructura_grado_estudios = gradoEstudios.text.toString(),
            reestructura_conocimiento_comp = conocimientoComp.text.toString(),
            reestructura_discapacidad = discapacidad.text.toString(),
            reestructura_dictamen = dictamen.text.toString(),
            reestructura_institucion_dictamen = institucionDictamen.text.toString(),
            reestructura_fecha_dictamen = fechaDictamen.text.toString(),
            reestructura_porcentaje_discapacidad = porcentajeDiscapacidad.text.toString(),
            reestructura_estado_civil = estadoCivil.text.toString(),
            reestructura_fecha_estado_civil = fechaEstadoCivil.text.toString(),
            reestructura_exesposo_aportacion = exesposoAportacion.text.toString(),
            reestructura_exesposo_monto = exesposoMonto.text.toString(),
            reestructura_regimen_conyugal = regimenConyugal.text.toString(),
            reestructura_vive_con_conyuge = viveConConyuge.text.toString(),
            reestructura_fecha_no_convive = fechaNoConvive.text.toString(),
            id_acreditado = idAcreditado!!

        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosReestrucutra(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte5Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte5Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte5Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte6Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
