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

class Formatoparte7SinConexion : AppCompatActivity() {

    private lateinit var integrantesEt: EditText
    private lateinit var totalOcupantesEt: EditText
    private lateinit var tipoFamiliaEt: EditText
    private lateinit var edad0_5HombresEt: EditText
    private lateinit var edad0_5MujeresEt: EditText
    private lateinit var edad6_12HombresEt: EditText
    private lateinit var edad6_12MujeresEt: EditText
    private lateinit var edad13_18HombresEt: EditText
    private lateinit var edad13_18MujeresEt: EditText
    private lateinit var edad19_35HombresEt: EditText
    private lateinit var edad19_35MujeresEt: EditText
    private lateinit var edad36_59HombresEt: EditText
    private lateinit var edad36_59MujeresEt: EditText
    private lateinit var edad60MasHombresEt: EditText
    private lateinit var edad60MasMujeresEt: EditText
    private lateinit var escuelaAsistenciaEt: EditText
    private lateinit var escolaridadNivelesEt: EditText
    private lateinit var familiaresEnfermedadEt: EditText
    private lateinit var familiaresEnfermedadCuantosEt: EditText
    private lateinit var familiaresEnfermedadQuienEt: EditText
    private lateinit var comprobanteEnfermedadEt: EditText
    private lateinit var tratamientoRecibidoEt: EditText
    private lateinit var tratamientoLugarEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte7_sin_conexion) // Asegúrate que este es el layout correcto

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""

        database = AppDatabase.getDatabase(this)

        // Inicializar EditTexts con los IDs de tu layout
        integrantesEt = findViewById(R.id.familia_integrantes)
        totalOcupantesEt = findViewById(R.id.familia_total_ocupantes)
        tipoFamiliaEt = findViewById(R.id.familia_tipo)
        edad0_5HombresEt = findViewById(R.id.edades_0_5_hombres)
        edad0_5MujeresEt = findViewById(R.id.edades_0_5_mujeres)
        edad6_12HombresEt = findViewById(R.id.edades_6_12_hombres)
        edad6_12MujeresEt = findViewById(R.id.edades_6_12_mujeres)
        edad13_18HombresEt = findViewById(R.id.edades_13_18_hombres)
        edad13_18MujeresEt = findViewById(R.id.edades_13_18_mujeres)
        edad19_35HombresEt = findViewById(R.id.edades_19_35_hombres)
        edad19_35MujeresEt = findViewById(R.id.edades_19_35_mujeres)
        edad36_59HombresEt = findViewById(R.id.edades_36_59_hombres)
        edad36_59MujeresEt = findViewById(R.id.edades_36_59_mujeres)
        edad60MasHombresEt = findViewById(R.id.edades_60_mas_hombres)
        edad60MasMujeresEt = findViewById(R.id.edades_60_mas_mujeres)
        escuelaAsistenciaEt = findViewById(R.id.escuela_asistencia)
        escolaridadNivelesEt = findViewById(R.id.escolaridad_niveles)
        familiaresEnfermedadEt = findViewById(R.id.familiares_enfermedad)
        familiaresEnfermedadCuantosEt = findViewById(R.id.familiares_enfermedad_cuantos)
        familiaresEnfermedadQuienEt = findViewById(R.id.familiares_enfermedad_quien)
        comprobanteEnfermedadEt = findViewById(R.id.comprobante_enfermedad)
        tratamientoRecibidoEt = findViewById(R.id.tratamiento_recibido)
        tratamientoLugarEt = findViewById(R.id.tratamiento_lugar)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosFamiliares()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarDatosFamiliares() {
        val datos = DatosFamiliaresEntity(
            familia_integrantes = integrantesEt.text.toString(),
            familia_total_ocupantes = totalOcupantesEt.text.toString(),
            familia_tipo = tipoFamiliaEt.text.toString(),
            edad_0_5_hombres = edad0_5HombresEt.text.toString(),
            edad_0_5_mujeres = edad0_5MujeresEt.text.toString(),
            edad_6_12_hombres = edad6_12HombresEt.text.toString(),
            edad_6_12_mujeres = edad6_12MujeresEt.text.toString(),
            edad_13_18_hombres = edad13_18HombresEt.text.toString(),
            edad_13_18_mujeres = edad13_18MujeresEt.text.toString(),
            edad_19_35_hombres = edad19_35HombresEt.text.toString(),
            edad_19_35_mujeres = edad19_35MujeresEt.text.toString(),
            edad_36_59_hombres = edad36_59HombresEt.text.toString(),
            edad_36_59_mujeres = edad36_59MujeresEt.text.toString(),
            edad_60_mas_hombres = edad60MasHombresEt.text.toString(),
            edad_60_mas_mujeres = edad60MasMujeresEt.text.toString(),
            escuela_asistencia = escuelaAsistenciaEt.text.toString(),
            escolaridad_niveles = escolaridadNivelesEt.text.toString(),
            familiares_enfermedad = familiaresEnfermedadEt.text.toString(),
            familiares_enfermedad_cuantos = familiaresEnfermedadCuantosEt.text.toString(),
            familiares_enfermedad_quien = familiaresEnfermedadQuienEt.text.toString(),
            comprobante_enfermedad = comprobanteEnfermedadEt.text.toString(),
            tratamiento_recibido = tratamientoRecibidoEt.text.toString(),
            tratamiento_lugar = tratamientoLugarEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosFamiliaresDao().insertDatosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte7SinConexion, "Datos guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte7SinConexion, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte8SinConexion::class.java)  // Cambia al nombre correcto de la siguiente activity
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
