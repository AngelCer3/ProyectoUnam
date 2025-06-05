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

class Formatoparte4SinConexion : AppCompatActivity() {

    private lateinit var creditoFechaEntrega: EditText
    private lateinit var creditoMonto: EditText
    private lateinit var creditoSueldoOtorgado: EditText
    private lateinit var creditoFechaUltimoPago: EditText
    private lateinit var creditoReciboPago: EditText
    private lateinit var creditoPagoActual: EditText
    private lateinit var creditoDeudaActual: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L  // recibido del intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte4_sin_conexion)

        idAcreditado = intent.getLongExtra("id_acreditado", 0L)
        database = AppDatabase.getDatabase(this)

        // Vincula vistas
        creditoFechaEntrega = findViewById(R.id.credito_fecha_entrega)
        creditoMonto = findViewById(R.id.credito_monto)
        creditoSueldoOtorgado = findViewById(R.id.credito_sueldo_otorgado)
        creditoFechaUltimoPago = findViewById(R.id.credito_fecha_ultimo_pago)
        creditoReciboPago = findViewById(R.id.credito_recibo_pago)
        creditoPagoActual = findViewById(R.id.credito_pago_actual)
        creditoDeudaActual = findViewById(R.id.credito_deuda_actual)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)

        guardarBtn.setOnClickListener {
            guardarDatosCredito()
        }

        siguienteBtn.setOnClickListener {
            irASiguiente()
        }
    }

    private fun guardarDatosCredito() {
        val datosCredito = DatosCreditoEntity(
            credito_fecha_entrega = creditoFechaEntrega.text.toString(),
            credito_monto = creditoMonto.text.toString(),
            credito_sueldo_otorgado = creditoSueldoOtorgado.text.toString(),
            credito_fecha_ultimo_pago = creditoFechaUltimoPago.text.toString(),
            credito_recibo_pago = creditoReciboPago.text.toString(),
            credito_pago_actual = creditoPagoActual.text.toString(),
            credito_deuda_actual = creditoDeudaActual.text.toString(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosCreditoDao().insertDatosCredito(datosCredito)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte4SinConexion, "Datos de crédito guardados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte4SinConexion, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte5SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
