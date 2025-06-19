package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class Formatoparte6SinConexion : AppCompatActivity() {

    private lateinit var conyugeNombre: EditText
    private lateinit var conyugeSexo: Spinner
    private lateinit var conyugeFechaNacimiento: EditText
    private lateinit var conyugeEdad: EditText
    private lateinit var conyugeGradoEstudios: EditText
    private lateinit var conyugeCompComputo: Spinner

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte6_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        // Inicializar base de datos
        database = AppDatabase.getDatabase(this)

        initViews()
        setupSpinners()
        setupButtons()
    }

    private fun initViews() {
        conyugeNombre = findViewById(R.id.conyuge_nombre)
        conyugeSexo = findViewById(R.id.conyuge_sexo)
        conyugeFechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        conyugeEdad = findViewById(R.id.conyuge_edad)
        conyugeGradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        conyugeCompComputo = findViewById(R.id.conyuge_comp_computo)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupSpinners() {
        val opcionesSexo = listOf("Seleccione", "Masculino", "Femenino", "Otro")
        conyugeSexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)

        val opcionesComp = listOf("Seleccione", "Ninguno", "Básico", "Intermedio", "Avanzado")
        conyugeCompComputo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesComp)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosConyuge()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosConyuge() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        if (conyugeNombre.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "El nombre del cónyuge es obligatorio",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            conyugeNombre.requestFocus()
            return false
        }

        // Validar que se seleccionó una opción válida en los spinners
        if (conyugeSexo.selectedItemPosition == 0) {
            mostrarDialogo(
                titulo = "Selección requerida",
                mensaje = "Debe seleccionar el sexo del cónyuge",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        if (conyugeCompComputo.selectedItemPosition == 0) {
            mostrarDialogo(
                titulo = "Selección requerida",
                mensaje = "Debe seleccionar el nivel de computación del cónyuge",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar formato de fecha (dd/MM/yyyy)
        val fechaStr = conyugeFechaNacimiento.text.toString().trim()
        if (fechaStr.isNotEmpty()) {
            try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaStr)
            } catch (e: Exception) {
                mostrarDialogo(
                    titulo = "Fecha inválida",
                    mensaje = "La fecha de nacimiento debe tener formato dd/MM/yyyy",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                conyugeFechaNacimiento.requestFocus()
                return false
            }
        }

        // Validar que la edad sea numérica y en rango válido (18-120)
        val edad = conyugeEdad.text.toString().trim().toIntOrNull()
        if (edad == null || edad < 18 || edad > 120) {
            mostrarDialogo(
                titulo = "Edad inválida",
                mensaje = "La edad debe ser un número entre 18 y 120 años",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            conyugeEdad.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatosConyuge() {
        val datos = DatosConyugeEntity(
            conyuge_nombre = conyugeNombre.text.toString().trim(),
            conyuge_sexo = conyugeSexo.selectedItem.toString(),
            conyuge_fecha_nacimiento = conyugeFechaNacimiento.text.toString().trim(),
            conyuge_edad = conyugeEdad.text.toString().trim(),
            conyuge_grado_estudios = conyugeGradoEstudios.text.toString().trim(),
            conyuge_comp_computo = conyugeCompComputo.selectedItem.toString(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existing = database.datosConyugeDao().getDatosConyugeByAcreditado(idAcreditado.toString())
                if (existing == null) {
                    database.datosConyugeDao().insertDatosConyuge(datos)
                } else {
                    // Actualizamos con el id para no generar nuevo registro
                    val updatedDatos = datos.copy(id_conyuge = existing.id_conyuge)
                    database.datosConyugeDao().updateDatosConyuge(updatedDatos)
                }
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos del cónyuge guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos del cónyuge: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte7SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado.toString())
        startActivity(intent)
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorYCerrar(mensaje: String) {
        mostrarDialogo(
            titulo = "Error crítico",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        ) {
            finish()
        }
    }

    private fun mostrarDialogo(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)

        view.findViewById<ImageView>(R.id.ivIcon).apply {
            setImageResource(iconoResId)
            setColorFilter(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvTitle).apply {
            text = titulo
            setTextColor(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvMessage).text = mensaje

        AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
            .apply {
                view.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    dismiss()
                    onAceptar?.invoke()
                }
                show()
            }
    }
}