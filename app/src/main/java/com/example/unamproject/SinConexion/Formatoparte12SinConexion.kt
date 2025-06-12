package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte12SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var idAcreditado: String

    // EditTexts
    private lateinit var familiaTieneDeudasEt: EditText
    private lateinit var familiaCantidadDeudaEt: EditText

    // Buttons
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte12_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        // Inicializar EditTexts
        familiaTieneDeudasEt = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeudaEt = findViewById(R.id.familia_cantidad_deuda)

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatosFamiliaDeudas()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Formatoparte13SinConexion::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun guardarDatosFamiliaDeudas() {
        val datos = DatosFamiliaDeudasEntity(
            familia_tiene_deudas = familiaTieneDeudasEt.text.toString(),
            familia_cantidad_deudas = familiaCantidadDeudaEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosFamiliaDeudasDao().insertDatosFamiliaDeudas(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte12SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte12SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
