package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte2SinConexion : AppCompatActivity() {

    private lateinit var visita1Fecha: EditText
    private lateinit var visita1Hora: EditText
    private lateinit var visita1Resultado: Spinner
    private lateinit var visita2Fecha: EditText
    private lateinit var visita2Hora: EditText
    private lateinit var visita2Resultado: Spinner
    private lateinit var visita3Fecha: EditText
    private lateinit var visita3Hora: EditText
    private lateinit var visita3Resultado: Spinner

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L  // recibido del intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte2_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getLongExtra("id_acreditado", 0L)

        // Inicializa la base de datos Room
        database = AppDatabase.getDatabase(this)

        // Vincula vistas
        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)
        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)
        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

        guardarBtn = findViewById(R.id.btnGuardarDatos)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarVisitas()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarVisitas() {
        val visitas = VisitasEntity(
            visita1_fecha = visita1Fecha.text.toString(),
            visita1_hora = visita1Hora.text.toString(),
            visita1_resultado = visita1Resultado.selectedItem.toString(),
            visita2_fecha = visita2Fecha.text.toString(),
            visita2_hora = visita2Hora.text.toString(),
            visita2_resultado = visita2Resultado.selectedItem.toString(),
            visita3_fecha = visita3Fecha.text.toString(),
            visita3_hora = visita3Hora.text.toString(),
            visita3_resultado = visita3Resultado.selectedItem.toString(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.visitasDao().insertVisita(visitas)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte2SinConexion, "Visitas guardadas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte2SinConexion, "Error al guardar visitas: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte3SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado.toString())
        startActivity(intent)
    }
}
