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

class Formatoparte13SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var idAcreditado: String

    // EditTexts
    private lateinit var telefono1LadaEt: EditText
    private lateinit var telefono1NumeroEt: EditText
    private lateinit var telefono1ExtensionEt: EditText
    private lateinit var telefono1TipoEt: EditText
    private lateinit var telefono2LadaEt: EditText
    private lateinit var telefono2NumeroEt: EditText
    private lateinit var telefono2ExtensionEt: EditText
    private lateinit var telefono2TipoEt: EditText

    // Buttons
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte13_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        // Inicializar EditTexts
        telefono1LadaEt = findViewById(R.id.telefono1_lada)
        telefono1NumeroEt = findViewById(R.id.telefono1_numero)
        telefono1ExtensionEt = findViewById(R.id.telefono1_extension)
        telefono1TipoEt = findViewById(R.id.telefono1_tipo)
        telefono2LadaEt = findViewById(R.id.telefono2_lada)
        telefono2NumeroEt = findViewById(R.id.telefono2_numero)
        telefono2ExtensionEt = findViewById(R.id.telefono2_extension)
        telefono2TipoEt = findViewById(R.id.telefono2_tipo)

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatosTelefonos()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Formatoparte14SinConexion::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun guardarDatosTelefonos() {
        val datos = DatosTelefonosEntity(
            telefono1_lada = telefono1LadaEt.text.toString(),
            telefono1_numero = telefono1NumeroEt.text.toString(),
            telefono1_extension = telefono1ExtensionEt.text.toString(),
            telefono1_tipo = telefono1TipoEt.text.toString(),
            telefono2_lada = telefono2LadaEt.text.toString(),
            telefono2_numero = telefono2NumeroEt.text.toString(),
            telefono2_extension = telefono2ExtensionEt.text.toString(),
            telefono2_tipo = telefono2TipoEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosTelefonosDao().insertDatosTelefonos(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte13SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte13SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
