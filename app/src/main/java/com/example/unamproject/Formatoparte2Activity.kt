package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte2Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

    // EditText y Spinners para capturar los datos de visitas
    private lateinit var visita1Fecha: EditText
    private lateinit var visita1Hora: EditText
    private lateinit var visita1Resultado: Spinner
    private lateinit var visita2Fecha: EditText
    private lateinit var visita2Hora: EditText
    private lateinit var visita2Resultado: Spinner
    private lateinit var visita3Fecha: EditText
    private lateinit var visita3Hora: EditText
    private lateinit var visita3Resultado: Spinner

    private var idAcreditado: String? = null
    private var idUsuario:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte2)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Inicializar campos
        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)

        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)

        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

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
        // Captura los valores de los EditText y Spinner
        val visita1FechaValue = visita1Fecha.text.toString()
        val visita1HoraValue = visita1Hora.text.toString()
        val visita1ResultadoValue = visita1Resultado.selectedItem.toString()

        val visita2FechaValue = visita2Fecha.text.toString()
        val visita2HoraValue = visita2Hora.text.toString()
        val visita2ResultadoValue = visita2Resultado.selectedItem.toString()

        val visita3FechaValue = visita3Fecha.text.toString()
        val visita3HoraValue = visita3Hora.text.toString()
        val visita3ResultadoValue = visita3Resultado.selectedItem.toString()

        // Crea un objeto con los datos
        val visitas = Visitas(
            visita1_fecha = visita1FechaValue,
            visita1_hora = visita1HoraValue,
            visita1_resultado = visita1ResultadoValue,
            visita2_fecha = visita2FechaValue,
            visita2_hora = visita2HoraValue,
            visita2_resultado = visita2ResultadoValue,
            visita3_fecha = visita3FechaValue,
            visita3_hora = visita3HoraValue,
            visita3_resultado = visita3ResultadoValue,
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        // Llamamos a la API para guardar los datos
        agregarVisitas(visitas)
    }

    // Función para enviar los datos a la API
    private fun agregarVisitas(visitas: Visitas) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarVisitas(visitas)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte2Activity, "Datos guardados exitosamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte2Activity, "Error al guardar datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte2Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte3Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}
