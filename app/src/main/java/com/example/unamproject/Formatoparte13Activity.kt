package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte13Activity : AppCompatActivity() {

    private lateinit var telefono1Lada: EditText
    private lateinit var telefono1Numero: EditText
    private lateinit var telefono1Extension: EditText
    private lateinit var telefono1Tipo: EditText
    private lateinit var telefono2Lada: EditText
    private lateinit var telefono2Numero: EditText
    private lateinit var telefono2Extension: EditText
    private lateinit var telefono2Tipo: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte13)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (validarCampos()) {
                irSiguiente()
            }
        }
    }

    private fun initViews() {
        telefono1Lada = findViewById(R.id.telefono1_lada)
        telefono1Numero = findViewById(R.id.telefono1_numero)
        telefono1Extension = findViewById(R.id.telefono1_extension)
        telefono1Tipo = findViewById(R.id.telefono1_tipo)
        telefono2Lada = findViewById(R.id.telefono2_lada)
        telefono2Numero = findViewById(R.id.telefono2_numero)
        telefono2Extension = findViewById(R.id.telefono2_extension)
        telefono2Tipo = findViewById(R.id.telefono2_tipo)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Validar al menos un teléfono completo
        val telefono1Completo = telefono1Numero.text.toString().isNotBlank()
        val telefono2Completo = telefono2Numero.text.toString().isNotBlank()

        if (!telefono1Completo && !telefono2Completo) {
            mostrarDialogoValidacion(
                "Datos requeridos",
                "Debe proporcionar al menos un número de teléfono",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            telefono1Numero.requestFocus()
            return false
        }

        // Validar teléfono 1 si tiene datos
        if (telefono1Completo) {
            if (telefono1Tipo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar el tipo del teléfono 1",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono1Tipo.requestFocus()
                return false
            }

            if (!telefono1Numero.text.toString().matches(Regex("\\d{7,10}"))) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "El número de teléfono 1 debe tener entre 7 y 10 dígitos",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono1Numero.requestFocus()
                return false
            }

            if (telefono1Lada.text.toString().isNotBlank() &&
                !telefono1Lada.text.toString().matches(Regex("\\d{2,3}"))) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La lada del teléfono 1 debe tener 2 o 3 dígitos",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono1Lada.requestFocus()
                return false
            }
        }

        // Validar teléfono 2 si tiene datos
        if (telefono2Completo) {
            if (telefono2Tipo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar el tipo del teléfono 2",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono2Tipo.requestFocus()
                return false
            }

            if (!telefono2Numero.text.toString().matches(Regex("\\d{7,10}"))) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "El número de teléfono 2 debe tener entre 7 y 10 dígitos",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono2Numero.requestFocus()
                return false
            }

            if (telefono2Lada.text.toString().isNotBlank() &&
                !telefono2Lada.text.toString().matches(Regex("\\d{2,3}"))) {
                mostrarDialogoValidacion(
                    "Formato inválido",
                    "La lada del teléfono 2 debe tener 2 o 3 dígitos",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                telefono2Lada.requestFocus()
                return false
            }
        }

        // Validar IDs
        if (idAcreditado.isNullOrBlank() || idUsuario.isNullOrBlank()) {
            mostrarDialogoValidacion(
                "Datos faltantes",
                "Faltan datos del acreditado o usuario",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val datos = datosTelefonos(
            telefono1_lada = telefono1Lada.text.toString(),
            telefono1_numero = telefono1Numero.text.toString(),
            telefono1_extension = telefono1Extension.text.toString(),
            telefono1_tipo = telefono1Tipo.text.toString(),
            telefono2_lada = telefono2Lada.text.toString(),
            telefono2_numero = telefono2Numero.text.toString(),
            telefono2_extension = telefono2Extension.text.toString(),
            telefono2_tipo = telefono2Tipo.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosTelefonicos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos telefónicos guardados correctamente",
                            android.R.drawable.ic_dialog_info,
                            0xFF388E3C.toInt()
                        )
                    } else {
                        mostrarDialogoValidacion(
                            "Error",
                            "Error al guardar los datos: ${response.message()}",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar al servidor: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte14Activity::class.java)
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