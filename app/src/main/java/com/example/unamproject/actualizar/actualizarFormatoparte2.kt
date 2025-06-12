package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.Visitas
import com.example.unamproject.identificarAcreditado
import kotlinx.coroutines.launch

class actualizarFormatoparte2 : AppCompatActivity() {

    private lateinit var visita1Fecha: EditText
    private lateinit var visita1Hora: EditText
    private lateinit var visita1Resultado: Spinner

    private lateinit var visita2Fecha: EditText
    private lateinit var visita2Hora: EditText
    private lateinit var visita2Resultado: Spinner

    private lateinit var visita3Fecha: EditText
    private lateinit var visita3Hora: EditText
    private lateinit var visita3Resultado: Spinner

    private lateinit var btnActualizarDatos: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null



    private lateinit var acreditado: identificarAcreditado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte2)



        // Vincular vistas
        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)

        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)

        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

        btnActualizarDatos = findViewById(R.id.btnActualizarDatos)

        // Obtener id_acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        idUsuario = intent.getStringExtra("id_usuario")


        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Cargar datos desde API
        cargarDatosPrevios()


        // Botón para actualizar
        btnActualizarDatos.setOnClickListener {
            actualizarDatos()
        }
        findViewById<Button>(R.id.btnSiguiente).setOnClickListener {
            navegarAParte3()
        }
    }

    private fun cargarDatosPrevios() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerVisitas(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val visitas = response.body()!!

                    visita1Fecha.setText(visitas.visita1_fecha)
                    visita1Hora.setText(visitas.visita1_hora)
                    setSpinnerSelection(visita1Resultado, visitas.visita1_resultado)

                    visita2Fecha.setText(visitas.visita2_fecha)
                    visita2Hora.setText(visitas.visita2_hora)
                    setSpinnerSelection(visita2Resultado, visitas.visita2_resultado)

                    visita3Fecha.setText(visitas.visita3_fecha)
                    visita3Hora.setText(visitas.visita3_hora)
                    setSpinnerSelection(visita3Resultado, visitas.visita3_resultado)

                } else {
                    Toast.makeText(this@actualizarFormatoparte2, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte2, "Error al obtener datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setSpinnerSelection(spinner: Spinner, value: String?) {
        if (value == null) return
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString().equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun actualizarDatos() {
        val visitas = Visitas(
            visita1_fecha = visita1Fecha.text.toString(),
            visita1_hora = visita1Hora.text.toString(),
            visita1_resultado = visita1Resultado.selectedItem?.toString() ?: "",
            visita2_fecha = visita2Fecha.text.toString(),
            visita2_hora = visita2Hora.text.toString(),
            visita2_resultado = visita2Resultado.selectedItem?.toString() ?: "",
            visita3_fecha = visita3Fecha.text.toString(),
            visita3_hora = visita3Hora.text.toString(),
            visita3_resultado = visita3Resultado.selectedItem?.toString() ?: "",
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarVisitas(idAcreditado, visitas)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte2, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte2, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte2, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navegarAParte3() {
        val intent = Intent(this, actualizarFormatoparte3::class.java).apply {
            putExtra("id_acreditado", idAcreditado) // o acreditado.id según tu data class
            putExtra("id_usuario", idUsuario)
        }
        startActivity(intent)
    }
}
