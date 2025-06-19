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
import java.text.SimpleDateFormat
import java.util.*

class Formatoparte6Activity : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var sexo: Spinner
    private lateinit var gradoEstudios: EditText
    private lateinit var compComputo: Spinner

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte6)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

        // Configurar adaptadores para Spinners
        val opcionesSexo = listOf("Masculino", "Femenino", "Otro")
        sexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)

        val opcionesComp = listOf("Ninguno", "Básico", "Intermedio", "Avanzado")
        compComputo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesComp)

        // Configurar listeners de botones
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
        nombre = findViewById(R.id.conyuge_nombre)
        fechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        edad = findViewById(R.id.conyuge_edad)
        sexo = findViewById(R.id.conyuge_sexo)
        gradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        compComputo = findViewById(R.id.conyuge_comp_computo)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        // Validar campos obligatorios
        val camposObligatorios = listOf(
            Pair(nombre, "Nombre"),
            Pair(fechaNacimiento, "Fecha de nacimiento"),
            Pair(edad, "Edad"),
            Pair(gradoEstudios, "Grado de estudios")
        )

        for ((campo, nombreCampo) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, completa el campo: $nombreCampo",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar fecha
        try {
            dateFormat.parse(fechaNacimiento.text.toString())
        } catch (e: Exception) {
            mostrarDialogoValidacion(
                "Fecha inválida",
                "Formato inválido en Fecha de nacimiento (usa formato dd/MM/yyyy)",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            fechaNacimiento.requestFocus()
            return false
        }

        // Validar que edad sea numérica
        if (edad.text.toString().toIntOrNull() == null) {
            mostrarDialogoValidacion(
                "Valor inválido",
                "El campo 'Edad' debe ser un número entero",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            edad.requestFocus()
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
        val datos = datosConyuge(
            conyuge_nombre = nombre.text.toString(),
            conyuge_fecha_nacimiento = fechaNacimiento.text.toString(),
            conyuge_edad = edad.text.toString(),
            conyuge_sexo = sexo.selectedItem.toString(),
            conyuge_grado_estudios = gradoEstudios.text.toString(),
            conyuge_comp_computo = compComputo.selectedItem.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosConyuge(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos guardados correctamente",
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
                        "No se pudo conectar: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte7Activity::class.java)
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