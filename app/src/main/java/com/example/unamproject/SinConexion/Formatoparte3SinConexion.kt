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

class Formatoparte3SinConexion : AppCompatActivity() {

    private lateinit var guardar: Button
    private lateinit var siguiente: Button

    private lateinit var viviendaLocalizada: EditText
    private lateinit var viviendaHabitada: EditText
    private lateinit var verificacionMetodo: EditText
    private lateinit var verificacionOtro: EditText
    private lateinit var vecinoNombre: EditText
    private lateinit var vecinoDireccion: EditText
    private lateinit var acreditadoVive: EditText
    private lateinit var jefeFamiliaNombre: EditText
    private lateinit var jefeFamiliaRelacion: EditText
    private lateinit var fechaOcupacion: EditText
    private lateinit var situacionVivienda: EditText
    private lateinit var documentoTraspaso: EditText
    private lateinit var tipoDocumentoTraspaso: EditText
    private lateinit var documentoMostrado: EditText
    private lateinit var documentoCopiaEntregada: EditText

    private lateinit var database: AppDatabase
    private var idAcreditado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte3_sin_conexion)

        database = AppDatabase.getDatabase(this)

        idAcreditado = intent.getLongExtra("id_acreditado", 0L).toString()

        // Referencias UI
        viviendaLocalizada = findViewById(R.id.vivienda_localizada)
        viviendaHabitada = findViewById(R.id.vivienda_habitada)
        verificacionMetodo = findViewById(R.id.verificacion_metodo)
        verificacionOtro = findViewById(R.id.verificacion_otro)
        vecinoNombre = findViewById(R.id.vecino_nombre)
        vecinoDireccion = findViewById(R.id.vecino_direccion)
        acreditadoVive = findViewById(R.id.acreditado_vive)
        jefeFamiliaNombre = findViewById(R.id.jefe_familia_nombre)
        jefeFamiliaRelacion = findViewById(R.id.jefe_familia_relacion)
        fechaOcupacion = findViewById(R.id.fecha_ocupacion)
        situacionVivienda = findViewById(R.id.situacion_vivienda)
        documentoTraspaso = findViewById(R.id.documento_traspaso)
        tipoDocumentoTraspaso = findViewById(R.id.tipo_documento_traspaso)
        documentoMostrado = findViewById(R.id.documento_mostrado)
        documentoCopiaEntregada = findViewById(R.id.documento_copia_entregada)

        guardar = findViewById(R.id.btnGuardar)
        siguiente = findViewById(R.id.btnSiguiente)

        guardar.setOnClickListener { guardarDatos() }

        siguiente.setOnClickListener {
            if (idAcreditado.isNotEmpty()) {
                irASiguiente()
            } else {
                Toast.makeText(this, "Primero guarda los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarDatos() {
        val datos = DatosViviendaEntity(
            vivienda_localizada = viviendaLocalizada.text.toString(),
            vivienda_habitada = viviendaHabitada.text.toString(),
            verificacion_metodo = verificacionMetodo.text.toString(),
            verificacion_otro = verificacionOtro.text.toString(),
            vecino_nombre = vecinoNombre.text.toString(),
            vecino_direccion = vecinoDireccion.text.toString(),
            acreditado_vive = acreditadoVive.text.toString(),
            jefe_familia_nombre = jefeFamiliaNombre.text.toString(),
            jefe_familia_relacion = jefeFamiliaRelacion.text.toString(),
            fecha_ocupacion = fechaOcupacion.text.toString(),
            situacion_vivienda = situacionVivienda.text.toString(),
            documento_traspaso = documentoTraspaso.text.toString(),
            tipo_documento_traspaso = tipoDocumentoTraspaso.text.toString(),
            documento_mostrado = documentoMostrado.text.toString(),
            documento_copia_entregada = documentoCopiaEntregada.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosViviendaDao().insertDatosVivienda(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte3SinConexion,
                        "Datos guardados correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Formatoparte3SinConexion,
                        "Error al guardar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte4SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado.toLong())
        startActivity(intent)
    }
}
