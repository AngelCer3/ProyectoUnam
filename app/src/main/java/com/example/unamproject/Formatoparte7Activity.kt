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

class Formatoparte7Activity : AppCompatActivity() {

    private lateinit var familia_integrantes: EditText
    private lateinit var familia_total_ocupantes: EditText
    private lateinit var familia_tipo: EditText
    private lateinit var edades_0_5_hombres: EditText
    private lateinit var edades_0_5_mujeres: EditText
    private lateinit var edades_6_12_hombres: EditText
    private lateinit var edades_6_12_mujeres: EditText
    private lateinit var edades_13_18_hombres: EditText
    private lateinit var edades_13_18_mujeres: EditText
    private lateinit var edades_19_35_hombres: EditText
    private lateinit var edades_19_35_mujeres: EditText
    private lateinit var edades_36_59_hombres: EditText
    private lateinit var edades_36_59_mujeres: EditText
    private lateinit var edades_60_mas_hombres: EditText
    private lateinit var edades_60_mas_mujeres: EditText
    private lateinit var escuela_asistencia: EditText
    private lateinit var escolaridad_niveles: EditText
    private lateinit var familiares_enfermedad: EditText
    private lateinit var familiares_enfermedad_cuantos: EditText
    private lateinit var familiares_enfermedad_quien: EditText
    private lateinit var comprobante_enfermedad: EditText
    private lateinit var tratamiento_recibido: EditText
    private lateinit var tratamiento_lugar: EditText
    private var idAcreditado: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte7)

        idAcreditado = intent.getStringExtra("id_acreditado")

        // Inicialización de campos
        familia_integrantes = findViewById(R.id.familia_integrantes)
        familia_total_ocupantes = findViewById(R.id.familia_total_ocupantes)
        familia_tipo = findViewById(R.id.familia_tipo)
        edades_0_5_hombres = findViewById(R.id.edades_0_5_hombres)
        edades_0_5_mujeres = findViewById(R.id.edades_0_5_mujeres)
        edades_6_12_hombres = findViewById(R.id.edades_6_12_hombres)
        edades_6_12_mujeres = findViewById(R.id.edades_6_12_mujeres)
        edades_13_18_hombres = findViewById(R.id.edades_13_18_hombres)
        edades_13_18_mujeres = findViewById(R.id.edades_13_18_mujeres)
        edades_19_35_hombres = findViewById(R.id.edades_19_35_hombres)
        edades_19_35_mujeres = findViewById(R.id.edades_19_35_mujeres)
        edades_36_59_hombres = findViewById(R.id.edades_36_59_hombres)
        edades_36_59_mujeres = findViewById(R.id.edades_36_59_mujeres)
        edades_60_mas_hombres = findViewById(R.id.edades_60_mas_hombres)
        edades_60_mas_mujeres = findViewById(R.id.edades_60_mas_mujeres)
        escuela_asistencia = findViewById(R.id.escuela_asistencia)
        escolaridad_niveles = findViewById(R.id.escolaridad_niveles)
        familiares_enfermedad = findViewById(R.id.familiares_enfermedad)
        familiares_enfermedad_cuantos = findViewById(R.id.familiares_enfermedad_cuantos)
        familiares_enfermedad_quien = findViewById(R.id.familiares_enfermedad_quien)
        comprobante_enfermedad = findViewById(R.id.comprobante_enfermedad)
        tratamiento_recibido = findViewById(R.id.tratamiento_recibido)
        tratamiento_lugar = findViewById(R.id.tratamiento_lugar)

        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            guardarDatos()
        }
        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)
        btnSiguiente.setOnClickListener {
            irSiguiente()
        }
    }

    private fun guardarDatos() {
        val datos = datosFamiliares(
            familia_integrantes.text.toString(),
            familia_total_ocupantes.text.toString(),
            familia_tipo.text.toString(),
            edades_0_5_hombres.text.toString(),
            edades_0_5_mujeres.text.toString(),
            edades_6_12_hombres.text.toString(),
            edades_6_12_mujeres.text.toString(),
            edades_13_18_hombres.text.toString(),
            edades_13_18_mujeres.text.toString(),
            edades_19_35_hombres.text.toString(),
            edades_19_35_mujeres.text.toString(),
            edades_36_59_hombres.text.toString(),
            edades_36_59_mujeres.text.toString(),
            edades_60_mas_hombres.text.toString(),
            edades_60_mas_mujeres.text.toString(),
            escuela_asistencia.text.toString(),
            escolaridad_niveles.text.toString(),
            familiares_enfermedad.text.toString(),
            familiares_enfermedad_cuantos.text.toString(),
            familiares_enfermedad_quien.text.toString(),
            comprobante_enfermedad.text.toString(),
            tratamiento_recibido.text.toString(),
            tratamiento_lugar.text.toString(),
            id_acreditado = idAcreditado!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosFamiliares(datos)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte7Activity, "Datos guardados correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte7Activity, "Error al guardar: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@Formatoparte7Activity, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte8Activity::class.java) // Cambia al siguiente Activity real
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
