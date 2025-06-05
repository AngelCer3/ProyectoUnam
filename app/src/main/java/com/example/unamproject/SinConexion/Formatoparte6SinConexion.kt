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

class Formatoparte6SinConexion : AppCompatActivity() {

    private lateinit var conyugeNombre: EditText
    private lateinit var conyugeSexo: EditText
    private lateinit var conyugeFechaNacimiento: EditText
    private lateinit var conyugeEdad: EditText
    private lateinit var conyugeGradoEstudios: EditText
    private lateinit var conyugeCompComputo: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L  // recibido del intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte6_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getLongExtra("id_acreditado", 0L)

        // Inicializa la base de datos Room
        database = AppDatabase.getDatabase(this)

        // Vincula vistas
        conyugeNombre = findViewById(R.id.conyuge_nombre)
        conyugeSexo = findViewById(R.id.conyuge_sexo)
        conyugeFechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        conyugeEdad = findViewById(R.id.conyuge_edad)
        conyugeGradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        conyugeCompComputo = findViewById(R.id.conyuge_comp_computo)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosConyuge()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }

        // Cargar datos previos si existen
        cargarDatosConyuge()
    }

    private fun guardarDatosConyuge() {
        val datos = DatosConyugeEntity(
            conyuge_nombre = conyugeNombre.text.toString(),
            conyuge_sexo = conyugeSexo.text.toString(),
            conyuge_fecha_nacimiento = conyugeFechaNacimiento.text.toString(),
            conyuge_edad = conyugeEdad.text.toString(),
            conyuge_grado_estudios = conyugeGradoEstudios.text.toString(),
            conyuge_comp_computo = conyugeCompComputo.text.toString(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existing = database.datosConyugeDao().getDatosConyugeByAcreditado(idAcreditado.toString())
                if (existing == null) {
                    database.datosConyugeDao().insertDatosConyuge(datos)
                } else {
                    // Actualizamos con el id para no generar nuevo registro
                    val updatedDatos = datos.copy(id_conyuge = existing.id_conyuge)
                    database.datosConyugeDao().updateDatosConyuge(updatedDatos)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte6SinConexion, "Datos de cónyuge guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte6SinConexion, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cargarDatosConyuge() {
        CoroutineScope(Dispatchers.IO).launch {
            val datos = database.datosConyugeDao().getDatosConyugeByAcreditado(idAcreditado.toString())
            if (datos != null) {
                withContext(Dispatchers.Main) {
                    conyugeNombre.setText(datos.conyuge_nombre)
                    conyugeSexo.setText(datos.conyuge_sexo)
                    conyugeFechaNacimiento.setText(datos.conyuge_fecha_nacimiento)
                    conyugeEdad.setText(datos.conyuge_edad)
                    conyugeGradoEstudios.setText(datos.conyuge_grado_estudios)
                    conyugeCompComputo.setText(datos.conyuge_comp_computo)
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte7SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
