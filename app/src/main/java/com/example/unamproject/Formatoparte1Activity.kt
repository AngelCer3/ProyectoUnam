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

class Formatoparte1Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

    // Declaración de los EditText para capturar los datos
    private lateinit var entidadFederativa: EditText
    private lateinit var ciudadMunicipio: EditText
    private lateinit var apellidoPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var nombres: EditText
    private lateinit var domicilioCalle: EditText
    private lateinit var domicilioCondominio: EditText
    private lateinit var domicilioIT: EditText
    private lateinit var domicilioMz: EditText
    private lateinit var domicilioNoExt: EditText
    private lateinit var domicilioNoInt: EditText
    private lateinit var domicilioEdif: EditText
    private lateinit var domicilioColonia: EditText
    private lateinit var domicilioCp: EditText
    private lateinit var domicilioCurp: EditText
    private var idAcreditado:String? = null
    private var idUsuario:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte1)

        idUsuario = intent.getStringExtra("id_usuario")
        // Asignación de los EditText
        entidadFederativa = findViewById(R.id.entidad_federativa)
        ciudadMunicipio = findViewById(R.id.ciudad_municipio_delegacion)
        apellidoPaterno = findViewById(R.id.apellido_paterno)
        apellidoMaterno = findViewById(R.id.apellido_materno)
        nombres = findViewById(R.id.nombres)
        domicilioCalle = findViewById(R.id.domicilio_calle)
        domicilioCondominio = findViewById(R.id.domicilio_condominio)
        domicilioIT = findViewById(R.id.domicilio_it)
        domicilioMz = findViewById(R.id.domicilio_mz)
        domicilioNoExt = findViewById(R.id.domicilio_no_ext)
        domicilioNoInt = findViewById(R.id.domicilio_no_int)
        domicilioEdif = findViewById(R.id.domicilio_edif)
        domicilioColonia = findViewById(R.id.domicilio_colonia)
        domicilioCp = findViewById(R.id.domicilio_cp)
        domicilioCurp = findViewById(R.id.domicilio_curp)

        // Botones
        siguiente = findViewById(R.id.btnSiguiente)
        guardar = findViewById(R.id.btnGuardarDatos)

        // Acción del botón "Guardar Datos"
        guardar.setOnClickListener {
            guardarDatos()
        }

        // Acción del botón "Siguiente"
        siguiente.setOnClickListener {
            siguienteFormato()
        }
    }

    private fun guardarDatos() {
        // Captura los valores de los EditText
        val entidadFederativaValue = entidadFederativa.text.toString()
        val ciudadMunicipioValue = ciudadMunicipio.text.toString()
        val apellidoPaternoValue = apellidoPaterno.text.toString()
        val apellidoMaternoValue = apellidoMaterno.text.toString()
        val nombresValue = nombres.text.toString()
        val domicilioCalleValue = domicilioCalle.text.toString()
        val domicilioCondominioValue = domicilioCondominio.text.toString()
        val domicilioITValue = domicilioIT.text.toString()
        val domicilioMzValue = domicilioMz.text.toString()
        val domicilioNoExtValue = domicilioNoExt.text.toString()
        val domicilioNoIntValue = domicilioNoInt.text.toString()
        val domicilioEdifValue = domicilioEdif.text.toString()
        val domicilioColoniaValue = domicilioColonia.text.toString()
        val domicilioCpValue = domicilioCp.text.toString()
        val domicilioCurpValue = domicilioCurp.text.toString()

        // Crea un objeto con los datos
        val acreditado = Acreditado(
            entidad_federativa = entidadFederativaValue,
            ciudad_municipio_delegacion = ciudadMunicipioValue,
            apellido_paterno = apellidoPaternoValue,
            apellido_materno = apellidoMaternoValue,
            nombres = nombresValue,
            domicilio_calle = domicilioCalleValue,
            domicilio_condominio = domicilioCondominioValue,
            domicilio_it = domicilioITValue,
            domicilio_mz = domicilioMzValue,
            domicilio_no_ext = domicilioNoExtValue,
            domicilio_no_int = domicilioNoIntValue,
            domicilio_edif = domicilioEdifValue,
            domicilio_colonia = domicilioColoniaValue,
            domicilio_cp = domicilioCpValue,
            domicilio_curp = domicilioCurpValue,
            id_usuario = idUsuario!!
        )

        // Llamamos a la API para guardar los datos
        agregarAcreditado(acreditado)
    }

    // Función para enviar los datos a la API
    private fun agregarAcreditado(acreditado: Acreditado) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarAcreditado(acreditado)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.success) {
                            idAcreditado = body.id_acreditado
                            Toast.makeText(this@Formatoparte1Activity, "Guardado correctamente. ID: $idAcreditado", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@Formatoparte1Activity, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Formatoparte1Activity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte1Activity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte2Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }

}
