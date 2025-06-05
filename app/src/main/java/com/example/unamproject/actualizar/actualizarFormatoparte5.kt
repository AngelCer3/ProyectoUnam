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
import com.example.unamproject.DatosReestructura
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import kotlinx.coroutines.launch

class actualizarFormatoparte5 : AppCompatActivity() {

    // Declara los EditText que correspondan con las variables de DatosReestructura
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

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte5)


        // Vincula los EditText con sus IDs en el layout
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

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        // Obtener id_acreditado desde Intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Cargar datos previos desde API
        cargarDatosPrevios()

        // Actualizar datos al dar click
        btnActualizarDatos.setOnClickListener {
            actualizarDatos()
        }

        // Navegar a siguiente actividad
        btnSiguiente.setOnClickListener {
            navegarASiguienteParte()
        }
    }

    private fun cargarDatosPrevios() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerReestructura(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    motivo.setText(data.reestructura_motivo)
                    documento.setText(data.reestructura_documento)
                    tipoDocumento.setText(data.reestructura_tipo_documento)
                    solicitanteEsAcred.setText(data.reestructura_solicitante_es_acred)
                    solicitanteNombre.setText(data.reestructura_solicitante_nombre)
                    parentesco.setText(data.reestructura_parentesco)
                    motivoPersonal.setText(data.reestructura_motivo_personal)
                    sexo.setText(data.reestructura_sexo)
                    fechaNacimiento.setText(data.reestructura_fecha_nacimiento)
                    edad.setText(data.reestructura_edad)
                    lugarNacimiento.setText(data.reestructura_lugar_nacimiento)
                    gradoEstudios.setText(data.reestructura_grado_estudios)
                    conocimientoComp.setText(data.reestructura_conocimiento_comp)
                    discapacidad.setText(data.reestructura_discapacidad)
                    dictamen.setText(data.reestructura_dictamen)
                    institucionDictamen.setText(data.reestructura_institucion_dictamen)
                    fechaDictamen.setText(data.reestructura_fecha_dictamen)
                    porcentajeDiscapacidad.setText(data.reestructura_porcentaje_discapacidad)
                    estadoCivil.setText(data.reestructura_estado_civil)
                    fechaEstadoCivil.setText(data.reestructura_fecha_estado_civil)
                    exesposoAportacion.setText(data.reestructura_exesposo_aportacion)
                    exesposoMonto.setText(data.reestructura_exesposo_monto)
                    regimenConyugal.setText(data.reestructura_regimen_conyugal)
                    viveConConyuge.setText(data.reestructura_vive_con_conyuge)
                    fechaNoConvive.setText(data.reestructura_fecha_no_convive)
                } else {
                    Toast.makeText(this@actualizarFormatoparte5, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte5, "Error al obtener datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatos() {
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
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosReestructura(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte5, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte5, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte5, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navegarASiguienteParte() {
        // Cambia actualizarFormatoparte6 por la siguiente actividad que corresponda
        val intent = Intent(this, actualizarFormatoparte6::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
