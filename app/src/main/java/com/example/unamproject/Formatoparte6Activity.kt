package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte6Activity : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var sexo: Spinner
    private lateinit var gradoEstudios: EditText
    private lateinit var compComputo: Spinner

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte6) // tu XML

        idAcreditado = intent.getStringExtra("id_acreditado")

        // Vincula views
        nombre = findViewById(R.id.conyuge_nombre)
        fechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        edad = findViewById(R.id.conyuge_edad)
        sexo = findViewById(R.id.conyuge_sexo)
        gradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        compComputo = findViewById(R.id.conyuge_comp_computo)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        // Llena los spinners (ejemplo)
        val opcionesSexo = listOf("Masculino", "Femenino", "Otro")
        sexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)

        val opcionesComp = listOf("Ninguno", "Básico", "Intermedio", "Avanzado")
        compComputo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesComp)



        // Botones
        btnGuardar.setOnClickListener {
            guardarDatos()
        }

        btnSiguiente.setOnClickListener {
            irSiguiente()
        }
    }

    private fun guardarDatos() {
        val datos = datosConyuge(
            conyuge_nombre = nombre.text.toString(),
            conyuge_fecha_nacimiento = fechaNacimiento.text.toString(),
            conyuge_edad = edad.text.toString(),
            conyuge_sexo = sexo.selectedItem.toString(),
            conyuge_grado_estudios = gradoEstudios.text.toString(),
            conyuge_comp_computo = compComputo.selectedItem.toString(),
            id_acreditado = idAcreditado!!

        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosConyuge(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte6Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte6Activity, "Error al guardar los datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte6Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte7Activity::class.java) // Cambia al siguiente Activity real
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
