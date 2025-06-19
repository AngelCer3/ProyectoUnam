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

class Formatoparte12Activity : AppCompatActivity() {

    private lateinit var familiaTieneDeudas: EditText
    private lateinit var familiaCantidadDeuda: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte12)
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
        familiaTieneDeudas = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeuda = findViewById(R.id.familia_cantidad_deuda)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Validar campo obligatorio (tiene deudas)
        if (familiaTieneDeudas.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si la familia tiene deudas",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            familiaTieneDeudas.requestFocus()
            return false
        }

        // Validación condicional: si hay deudas, cantidad es obligatoria
        val tieneDeudas = familiaTieneDeudas.text.toString().equals("Sí", ignoreCase = true) ||
                familiaTieneDeudas.text.toString().equals("Si", ignoreCase = true)

        if (tieneDeudas && familiaCantidadDeuda.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar la cantidad de deuda",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            familiaCantidadDeuda.requestFocus()
            return false
        }

        // Validar que la cantidad de deuda sea numérica si existe
        if (tieneDeudas && familiaCantidadDeuda.text.toString().toDoubleOrNull() == null) {
            mostrarDialogoValidacion(
                "Valor inválido",
                "La cantidad de deuda debe ser un valor numérico",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            familiaCantidadDeuda.requestFocus()
            return false
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
        val datos = datosFamiliaDeudas(
            familia_tiene_deudas = familiaTieneDeudas.text.toString(),
            familia_cantidad_deudas = familiaCantidadDeuda.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosFamiliaDeudas(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos de deudas familiares guardados correctamente",
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
        val intent = Intent(this, Formatoparte13Activity::class.java)
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