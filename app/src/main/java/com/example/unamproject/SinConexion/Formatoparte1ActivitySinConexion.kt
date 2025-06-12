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


class Formatoparte1ActivitySinConexion : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

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

    private lateinit var database: AppDatabase

    // Aquí guardamos el ID generado por Room después de insertar
    private var idAcreditado: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte1_sin_conexion)

        // Inicializa la base de datos Room
        database = AppDatabase.getDatabase(this)

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

        guardar = findViewById(R.id.btnGuardarDatos)
        siguiente = findViewById(R.id.btnSiguiente)

        guardar.setOnClickListener {
            guardarDatos()
        }

        siguiente.setOnClickListener {
            if (idAcreditado != 0L) {
                irASiguiente()
            } else {
                Toast.makeText(this, "Primero guarda los datos para continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarDatos() {
        val acreditado = AcreditadoEntity(
            entidad_federativa = entidadFederativa.text.toString(),
            ciudad_municipio_delegacion = ciudadMunicipio.text.toString(),
            apellido_paterno = apellidoPaterno.text.toString(),
            apellido_materno = apellidoMaterno.text.toString(),
            nombres = nombres.text.toString(),
            domicilio_calle = domicilioCalle.text.toString(),
            domicilio_condominio = domicilioCondominio.text.toString(),
            domicilio_it = domicilioIT.text.toString(),
            domicilio_mz = domicilioMz.text.toString(),
            domicilio_no_ext = domicilioNoExt.text.toString(),
            domicilio_no_int = domicilioNoInt.text.toString(),
            domicilio_edif = domicilioEdif.text.toString(),
            domicilio_colonia = domicilioColonia.text.toString(),
            domicilio_cp = domicilioCp.text.toString(),
            domicilio_curp = domicilioCurp.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = database.acreditadoDao().insertAcreditado(acreditado)
                idAcreditado = id
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte1ActivitySinConexion,
                        "Datos guardados. ID: $idAcreditado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte1ActivitySinConexion,
                        "Error al guardar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte2SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
