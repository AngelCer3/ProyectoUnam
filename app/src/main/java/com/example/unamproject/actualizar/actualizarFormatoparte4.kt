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
import com.example.unamproject.datosCredito
import kotlinx.coroutines.launch

class actualizarFormatoparte4 : AppCompatActivity() {

    private lateinit var creditoFechaEntrega: EditText
    private lateinit var creditoMonto: EditText
    private lateinit var creditoSueldoOtorgado: EditText
    private lateinit var creditoFechaUltimoPago: EditText
    private lateinit var creditoReciboPago: EditText
    private lateinit var creditoPagoActual: EditText
    private lateinit var creditoDeudaActual: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte4)


        // Vincular vistas
        creditoFechaEntrega = findViewById(R.id.credito_fecha_entrega)
        creditoMonto = findViewById(R.id.credito_monto)
        creditoSueldoOtorgado = findViewById(R.id.credito_sueldo_otorgado)
        creditoFechaUltimoPago = findViewById(R.id.credito_fecha_ultimo_pago)
        creditoReciboPago = findViewById(R.id.credito_recibo_pago)
        creditoPagoActual = findViewById(R.id.credito_pago_actual)
        creditoDeudaActual = findViewById(R.id.credito_deuda_actual)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        // Obtener id_acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Cargar datos previos desde API
        cargarDatosPrevios()

        // Configurar botón actualizar
        btnActualizarDatos.setOnClickListener {
            actualizarDatos()
        }

        // Botón siguiente
        btnSiguiente.setOnClickListener {
            navegarAParteSiguiente()
        }
    }

    private fun cargarDatosPrevios() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerCredito(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val credito = response.body()!!
                    creditoFechaEntrega.setText(credito.credito_fecha_entrega)
                    creditoMonto.setText(credito.credito_monto)
                    creditoSueldoOtorgado.setText(credito.credito_sueldo_otorgado)
                    creditoFechaUltimoPago.setText(credito.credito_fecha_ultimo_pago)
                    creditoReciboPago.setText(credito.credito_recibo_pago)
                    creditoPagoActual.setText(credito.credito_pago_actual)
                    creditoDeudaActual.setText(credito.credito_deuda_actual)
                } else {
                    Toast.makeText(this@actualizarFormatoparte4, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte4, "Error al obtener datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatos() {
        val credito = datosCredito(
            credito_fecha_entrega = creditoFechaEntrega.text.toString(),
            credito_monto = creditoMonto.text.toString(),
            credito_sueldo_otorgado = creditoSueldoOtorgado.text.toString(),
            credito_fecha_ultimo_pago = creditoFechaUltimoPago.text.toString(),
            credito_recibo_pago = creditoReciboPago.text.toString(),
            credito_pago_actual = creditoPagoActual.text.toString(),
            credito_deuda_actual = creditoDeudaActual.text.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosCredito(idAcreditado, credito)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte4, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte4, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte4, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navegarAParteSiguiente() {
        // Cambia actualizarFormatoparte5 por la siguiente actividad que corresponda
        val intent = Intent(this, actualizarFormatoparte5::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
