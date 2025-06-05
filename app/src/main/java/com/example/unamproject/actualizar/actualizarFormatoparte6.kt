package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosConyuge
import kotlinx.coroutines.launch

class actualizarFormatoparte6 : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var sexo: Spinner
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var gradoEstudios: EditText
    private lateinit var compComputo: Spinner

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    private val opcionesSexo = listOf("Masculino", "Femenino", "Otro")
    private val opcionesComp = listOf("Ninguno", "Básico", "Intermedio", "Avanzado")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte6)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vincular EditText y Spinners con layout
        nombre = findViewById(R.id.conyuge_nombre)
        sexo = findViewById(R.id.conyuge_sexo)
        fechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        edad = findViewById(R.id.conyuge_edad)
        gradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        compComputo = findViewById(R.id.conyuge_comp_computo)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Inicializar adaptadores de los spinners
        sexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)
        compComputo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesComp)

        cargarDatos()

        btnActualizarDatos.setOnClickListener {
            actualizarDatos()
        }

        btnSiguiente.setOnClickListener {
            irASiguiente()
        }
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosConyuge(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!

                    // Setear textos en EditText
                    nombre.setText(datos.conyuge_nombre)
                    fechaNacimiento.setText(datos.conyuge_fecha_nacimiento)
                    edad.setText(datos.conyuge_edad)
                    gradoEstudios.setText(datos.conyuge_grado_estudios)

                    // Seleccionar en Spinner sexo
                    val adapterSexo = sexo.adapter as ArrayAdapter<String>
                    val posSexo = adapterSexo.getPosition(datos.conyuge_sexo)
                    if (posSexo >= 0) sexo.setSelection(posSexo)

                    // Seleccionar en Spinner compComputo
                    val adapterComp = compComputo.adapter as ArrayAdapter<String>
                    val posComp = adapterComp.getPosition(datos.conyuge_comp_computo)
                    if (posComp >= 0) compComputo.setSelection(posComp)

                } else {
                    Toast.makeText(this@actualizarFormatoparte6, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte6, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatos() {
        val datos = datosConyuge(
            conyuge_nombre = nombre.text.toString(),
            conyuge_sexo = sexo.selectedItem.toString(),
            conyuge_fecha_nacimiento = fechaNacimiento.text.toString(),
            conyuge_edad = edad.text.toString(),
            conyuge_grado_estudios = gradoEstudios.text.toString(),
            conyuge_comp_computo = compComputo.selectedItem.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosConyuge(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte6, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte6, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte6, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte7::class.java) // Cambia al siguiente activity
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
