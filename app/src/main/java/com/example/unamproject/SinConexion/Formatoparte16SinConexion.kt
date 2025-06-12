package com.example.unamproject.SinConexion

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte16SinConexion : AppCompatActivity() {

    private lateinit var viviendaNumeroHabitaciones: EditText
    private lateinit var viviendaTipoPiso: EditText
    private lateinit var viviendaTipoPisoOtro: EditText
    private lateinit var viviendaTipoTecho: EditText
    private lateinit var viviendaCuentaBano: EditText
    private lateinit var observacionesEntrevistador: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var db: AppDatabase
    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte16_sin_conexion)


        // Inicialización de vistas
        viviendaNumeroHabitaciones = findViewById(R.id.vivienda_numero_habitaciones)
        viviendaTipoPiso = findViewById(R.id.vivienda_tipo_piso)
        viviendaTipoPisoOtro = findViewById(R.id.vivienda_tipo_piso_otro)
        viviendaTipoTecho = findViewById(R.id.vivienda_tipo_techo)
        viviendaCuentaBano = findViewById(R.id.viviendo_cuenta_bano)
        observacionesEntrevistador = findViewById(R.id.observaciones_entrevistador)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        db = AppDatabase.getDatabase(this)
        idAcreditado = intent.getStringExtra("id_acreditado") ?: "sin_id"

        btnGuardar.setOnClickListener {
            guardarDatos()
        }
        btnSiguiente.setOnClickListener {
            val intent = Intent(this, FormatoparteFinalSinConexion::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }


        btnSiguiente.setOnClickListener {
            Toast.makeText(this, "Datos guardados. Continuar a la siguiente pantalla.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarDatos() {
        val datosVivienda = DatosEspecificosViviendaEntity(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString(),
            id_acreditado = idAcreditado
        )

        val datosObservaciones = DatosObservacionesEntity(
            observaciones_entrevistador = observacionesEntrevistador.text.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.datosEspecificosViviendaDao().insertDatosEspecificosVivienda(datosVivienda)
                db.datosObservacionesDao().insertDatosObservaciones(datosObservaciones)
            }

            Toast.makeText(this@Formatoparte16SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
        }
    }
}
