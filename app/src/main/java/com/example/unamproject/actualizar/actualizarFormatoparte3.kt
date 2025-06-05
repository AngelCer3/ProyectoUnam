package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosVivienda
import kotlinx.coroutines.launch

class actualizarFormatoparte3 : AppCompatActivity() {

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

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte3)

        // Vincular vistas
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

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        // Spinner: cargar opciones
        val opciones = resources.getStringArray(R.array.situacion_vivienda_opciones)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        situacionVivienda.adapter = adapter

        // Obtener id_acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosPrevios()

        btnActualizarDatos.setOnClickListener {
            actualizarDatos()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, actualizarFormatoparte4::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun cargarDatosPrevios() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerVivienda(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val vivienda = response.body()!!

                    viviendaLocalizada.setText(vivienda.vivienda_localizada)
                    viviendaHabitada.setText(vivienda.vivienda_habitada)
                    verificacionMetodo.setText(vivienda.verificacion_metodo)
                    verificacionOtro.setText(vivienda.verificacion_otro)
                    vecinoNombre.setText(vivienda.vecino_nombre)
                    vecinoDireccion.setText(vivienda.vecino_direccion)
                    acreditadoVive.setText(vivienda.acreditado_vive)
                    jefeFamiliaNombre.setText(vivienda.jefe_familia_nombre)
                    jefeFamiliaRelacion.setText(vivienda.jefe_familia_relacion)
                    fechaOcupacion.setText(vivienda.fecha_ocupacion)

                    val index = (situacionVivienda.adapter as ArrayAdapter<String>)
                        .getPosition(vivienda.situacion_vivienda)
                    situacionVivienda.setSelection(index)

                    documentoTraspaso.setText(vivienda.documento_traspaso)
                    tipoDocumentoTraspaso.setText(vivienda.tipo_documento_traspaso)
                    documentoMostrado.setText(vivienda.documento_mostrado)
                    documentoCopiaEntregada.setText(vivienda.documento_copia_entregada)

                } else {
                    Toast.makeText(this@actualizarFormatoparte3, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte3, "Error al obtener datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatos() {
        val vivienda = datosVivienda(
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
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosVivienda(idAcreditado, vivienda)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte3, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte3, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte3, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
