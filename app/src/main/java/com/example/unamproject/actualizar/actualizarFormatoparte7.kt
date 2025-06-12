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
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosFamiliares
import kotlinx.coroutines.launch

class actualizarFormatoparte7 : AppCompatActivity() {

    private lateinit var familiaIntegrantes: EditText
    private lateinit var familiaTotalOcupantes: EditText
    private lateinit var familiaTipo: EditText
    private lateinit var edad0_5Hombres: EditText
    private lateinit var edad0_5Mujeres: EditText
    private lateinit var edad6_12Hombres: EditText
    private lateinit var edad6_12Mujeres: EditText
    private lateinit var edad13_18Hombres: EditText
    private lateinit var edad13_18Mujeres: EditText
    private lateinit var edad19_35Hombres: EditText
    private lateinit var edad19_35Mujeres: EditText
    private lateinit var edad36_59Hombres: EditText
    private lateinit var edad36_59Mujeres: EditText
    private lateinit var edad60MasHombres: EditText
    private lateinit var edad60MasMujeres: EditText
    private lateinit var escuelaAsistencia: EditText
    private lateinit var escolaridadNiveles: EditText
    private lateinit var familiaresEnfermedad: EditText
    private lateinit var familiaresEnfermedadCuantos: EditText
    private lateinit var familiaresEnfermedadQuien: EditText
    private lateinit var comprobanteEnfermedad: EditText
    private lateinit var tratamientoRecibido: EditText
    private lateinit var tratamientoLugar: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte7)

        // Vincular EditText con sus IDs en el layout
        familiaIntegrantes = findViewById(R.id.familia_integrantes)
        familiaTotalOcupantes = findViewById(R.id.familia_total_ocupantes)
        familiaTipo = findViewById(R.id.familia_tipo)
        edad0_5Hombres = findViewById(R.id.edades_0_5_hombres)
        edad0_5Mujeres = findViewById(R.id.edades_0_5_mujeres)
        edad6_12Hombres = findViewById(R.id.edades_6_12_hombres)
        edad6_12Mujeres = findViewById(R.id.edades_6_12_mujeres)
        edad13_18Hombres = findViewById(R.id.edades_13_18_hombres)
        edad13_18Mujeres = findViewById(R.id.edades_13_18_mujeres)
        edad19_35Hombres = findViewById(R.id.edades_19_35_hombres)
        edad19_35Mujeres = findViewById(R.id.edades_19_35_mujeres)
        edad36_59Hombres = findViewById(R.id.edades_36_59_hombres)
        edad36_59Mujeres = findViewById(R.id.edades_36_59_mujeres)
        edad60MasHombres = findViewById(R.id.edades_60_mas_hombres)
        edad60MasMujeres = findViewById(R.id.edades_60_mas_mujeres)
        escuelaAsistencia = findViewById(R.id.escuela_asistencia)
        escolaridadNiveles = findViewById(R.id.escolaridad_niveles)
        familiaresEnfermedad = findViewById(R.id.familiares_enfermedad)
        familiaresEnfermedadCuantos = findViewById(R.id.familiares_enfermedad_cuantos)
        familiaresEnfermedadQuien = findViewById(R.id.familiares_enfermedad_quien)
        comprobanteEnfermedad = findViewById(R.id.comprobante_enfermedad)
        tratamientoRecibido = findViewById(R.id.tratamiento_recibido)
        tratamientoLugar = findViewById(R.id.tratamiento_lugar)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

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
                val response = RetrofitClient.webService.obtenerDatosFamiliares(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    familiaIntegrantes.setText(datos.familia_integrantes)
                    familiaTotalOcupantes.setText(datos.familia_total_ocupantes)
                    familiaTipo.setText(datos.familia_tipo)
                    edad0_5Hombres.setText(datos.edad_0_5_hombres)
                    edad0_5Mujeres.setText(datos.edad_0_5_mujeres)
                    edad6_12Hombres.setText(datos.edad_6_12_hombres)
                    edad6_12Mujeres.setText(datos.edad_6_12_mujeres)
                    edad13_18Hombres.setText(datos.edad_13_18_hombres)
                    edad13_18Mujeres.setText(datos.edad_13_18_mujeres)
                    edad19_35Hombres.setText(datos.edad_19_35_hombres)
                    edad19_35Mujeres.setText(datos.edad_19_35_mujeres)
                    edad36_59Hombres.setText(datos.edad_36_59_hombres)
                    edad36_59Mujeres.setText(datos.edad_36_59_mujeres)
                    edad60MasHombres.setText(datos.edad_60_mas_hombres)
                    edad60MasMujeres.setText(datos.edad_60_mas_mujeres)
                    escuelaAsistencia.setText(datos.escuela_asistencia)
                    escolaridadNiveles.setText(datos.escolaridad_niveles)
                    familiaresEnfermedad.setText(datos.familiares_enfermedad)
                    familiaresEnfermedadCuantos.setText(datos.familiares_enfermedad_cuantos)
                    familiaresEnfermedadQuien.setText(datos.familiares_enfermedad_quien)
                    comprobanteEnfermedad.setText(datos.comprobante_enfermedad)
                    tratamientoRecibido.setText(datos.tratamiento_recibido)
                    tratamientoLugar.setText(datos.tratamiento_lugar)
                } else {
                    Toast.makeText(this@actualizarFormatoparte7, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte7, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatos() {
        val datos = datosFamiliares(
            familia_integrantes = familiaIntegrantes.text.toString(),
            familia_total_ocupantes = familiaTotalOcupantes.text.toString(),
            familia_tipo = familiaTipo.text.toString(),
            edad_0_5_hombres = edad0_5Hombres.text.toString(),
            edad_0_5_mujeres = edad0_5Mujeres.text.toString(),
            edad_6_12_hombres = edad6_12Hombres.text.toString(),
            edad_6_12_mujeres = edad6_12Mujeres.text.toString(),
            edad_13_18_hombres = edad13_18Hombres.text.toString(),
            edad_13_18_mujeres = edad13_18Mujeres.text.toString(),
            edad_19_35_hombres = edad19_35Hombres.text.toString(),
            edad_19_35_mujeres = edad19_35Mujeres.text.toString(),
            edad_36_59_hombres = edad36_59Hombres.text.toString(),
            edad_36_59_mujeres = edad36_59Mujeres.text.toString(),
            edad_60_mas_hombres = edad60MasHombres.text.toString(),
            edad_60_mas_mujeres = edad60MasMujeres.text.toString(),
            escuela_asistencia = escuelaAsistencia.text.toString(),
            escolaridad_niveles = escolaridadNiveles.text.toString(),
            familiares_enfermedad = familiaresEnfermedad.text.toString(),
            familiares_enfermedad_cuantos = familiaresEnfermedadCuantos.text.toString(),
            familiares_enfermedad_quien = familiaresEnfermedadQuien.text.toString(),
            comprobante_enfermedad = comprobanteEnfermedad.text.toString(),
            tratamiento_recibido = tratamientoRecibido.text.toString(),
            tratamiento_lugar = tratamientoLugar.text.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosFamiliares(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte7, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte7, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte7, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte8::class.java) // Cambiar siguienteActivity por la actividad que corresponda
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
