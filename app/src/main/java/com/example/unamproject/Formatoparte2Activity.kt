package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Formatoparte2Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

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
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte2)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)

        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)

        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

        siguiente = findViewById(R.id.btnSiguiente)
        guardar = findViewById(R.id.btnGuardarDatos)

        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
            if (validarCampos()) {
                siguienteFormato()
            }
        }
    }

    private fun validarCampos(): Boolean {
        if (visita1Fecha.text.toString().trim().isEmpty()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Por favor, ingresa la fecha de la visita 1",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            visita1Fecha.requestFocus()
            return false
        } else if (!esFechaValida(visita1Fecha.text.toString().trim())) {
            mostrarDialogoValidacion(
                "Formato inválido",
                "La fecha de la visita 1 no es válida. Usa formato dd/MM/yyyy",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            visita1Fecha.requestFocus()
            return false
        }

        if (visita1Hora.text.toString().trim().isEmpty()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Por favor, ingresa la hora de la visita 1",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            visita1Hora.requestFocus()
            return false
        } else if (!esHoraValida(visita1Hora.text.toString().trim())) {
            mostrarDialogoValidacion(
                "Formato inválido",
                "La hora de la visita 1 no es válida. Usa formato HH:mm (24 horas)",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            visita1Hora.requestFocus()
            return false
        }

        if (visita1Resultado.selectedItemPosition == 0) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Por favor, selecciona un resultado para la visita 1",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            visita1Resultado.requestFocus()
            return false
        }

        // Visita 2 (opcional)
        val v2Fecha = visita2Fecha.text.toString().trim()
        val v2Hora = visita2Hora.text.toString().trim()
        val v2ResPos = visita2Resultado.selectedItemPosition
        val v2CamposLlenos = (v2Fecha.isNotEmpty() || v2Hora.isNotEmpty() || v2ResPos != 0)

        if (v2CamposLlenos) {
            if (v2Fecha.isEmpty()) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 2, ingresa la fecha",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita2Fecha.requestFocus()
                return false
            } else if (!esFechaValida(v2Fecha)) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La fecha de la visita 2 no es válida. Usa formato dd/MM/yyyy",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita2Fecha.requestFocus()
                return false
            }

            if (v2Hora.isEmpty()) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 2, ingresa la hora",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita2Hora.requestFocus()
                return false
            } else if (!esHoraValida(v2Hora)) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La hora de la visita 2 no es válida. Usa formato HH:mm (24 horas)",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita2Hora.requestFocus()
                return false
            }

            if (v2ResPos == 0) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 2, selecciona un resultado",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita2Resultado.requestFocus()
                return false
            }
        }

        // Visita 3 (opcional)
        val v3Fecha = visita3Fecha.text.toString().trim()
        val v3Hora = visita3Hora.text.toString().trim()
        val v3ResPos = visita3Resultado.selectedItemPosition
        val v3CamposLlenos = (v3Fecha.isNotEmpty() || v3Hora.isNotEmpty() || v3ResPos != 0)

        if (v3CamposLlenos) {
            if (v3Fecha.isEmpty()) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 3, ingresa la fecha",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita3Fecha.requestFocus()
                return false
            } else if (!esFechaValida(v3Fecha)) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La fecha de la visita 3 no es válida. Usa formato dd/MM/yyyy",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita3Fecha.requestFocus()
                return false
            }

            if (v3Hora.isEmpty()) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 3, ingresa la hora",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita3Hora.requestFocus()
                return false
            } else if (!esHoraValida(v3Hora)) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La hora de la visita 3 no es válida. Usa formato HH:mm (24 horas)",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita3Hora.requestFocus()
                return false
            }

            if (v3ResPos == 0) {
                mostrarDialogoValidacion(
                    "Campo incompleto",
                    "Si llenas visita 3, selecciona un resultado",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                visita3Resultado.requestFocus()
                return false
            }
        }

        return true
    }

    private fun esFechaValida(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formato.isLenient = false
        return try {
            formato.parse(fecha)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun esHoraValida(hora: String): Boolean {
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatoHora.isLenient = false
        return try {
            formatoHora.parse(hora)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun guardarDatos() {
        val visita1FechaValue = visita1Fecha.text.toString()
        val visita1HoraValue = visita1Hora.text.toString()
        val visita1ResultadoValue = visita1Resultado.selectedItem.toString()

        val visita2FechaValue = visita2Fecha.text.toString()
        val visita2HoraValue = visita2Hora.text.toString()
        val visita2ResultadoValue = visita2Resultado.selectedItem.toString()

        val visita3FechaValue = visita3Fecha.text.toString()
        val visita3HoraValue = visita3Hora.text.toString()
        val visita3ResultadoValue = visita3Resultado.selectedItem.toString()

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

        agregarVisitas(visitas)
    }

    private fun agregarVisitas(visitas: Visitas) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarVisitas(visitas)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos guardados exitosamente",
                            android.R.drawable.ic_dialog_info,
                            0xFF388E3C.toInt()
                        )
                    } else {
                        mostrarDialogoValidacion(
                            "Error",
                            "Error al guardar datos",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
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

    private fun mostrarDialogoValidacion(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val icon = view.findViewById<ImageView>(R.id.ivIcon)
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val message = view.findViewById<TextView>(R.id.tvMessage)
        val btnOk = view.findViewById<Button>(R.id.btnOk)

        icon.setImageResource(iconoResId)
        icon.setColorFilter(colorTitulo)
        title.text = titulo
        title.setTextColor(colorTitulo)
        message.text = mensaje

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
            onAceptar?.invoke()
        }

        dialog.show()
    }
}
