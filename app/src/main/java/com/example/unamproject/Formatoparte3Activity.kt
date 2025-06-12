package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte3Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

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
    private lateinit var situacionVivienda: Spinner
    private lateinit var documentoTraspaso: EditText
    private lateinit var tipoDocumentoTraspaso: EditText
    private lateinit var documentoMostrado: EditText
    private lateinit var documentoCopiaEntregada: EditText

    private var idAcreditado: String? = null
    private var idUsuario:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte3)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

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

        siguiente = findViewById(R.id.btnSiguiente)
        guardar = findViewById(R.id.btnGuardar)

        guardar.setOnClickListener {
            guardarDatos()
        }

        siguiente.setOnClickListener {
            siguienteFormato()
        }
    }

    private fun guardarDatos() {
        val datosVivienda = datosVivienda(
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
            situacion_vivienda = situacionVivienda.selectedItem.toString(),
            documento_traspaso = documentoTraspaso.text.toString(),
            tipo_documento_traspaso = tipoDocumentoTraspaso.text.toString(),
            documento_mostrado = documentoMostrado.text.toString(),
            documento_copia_entregada = documentoCopiaEntregada.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosVivienda(datosVivienda)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Formatoparte3Activity, "Datos guardados exitosamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Formatoparte3Activity, "Error al guardar datos", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte3Activity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte4Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }
}