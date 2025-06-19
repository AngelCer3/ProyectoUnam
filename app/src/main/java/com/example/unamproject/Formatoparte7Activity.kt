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

class Formatoparte7Activity : AppCompatActivity() {

    private lateinit var familia_integrantes: EditText
    private lateinit var familia_total_ocupantes: EditText
    private lateinit var familia_tipo: EditText
    private lateinit var edades_0_5_hombres: EditText
    private lateinit var edades_0_5_mujeres: EditText
    private lateinit var edades_6_12_hombres: EditText
    private lateinit var edades_6_12_mujeres: EditText
    private lateinit var edades_13_18_hombres: EditText
    private lateinit var edades_13_18_mujeres: EditText
    private lateinit var edades_19_35_hombres: EditText
    private lateinit var edades_19_35_mujeres: EditText
    private lateinit var edades_36_59_hombres: EditText
    private lateinit var edades_36_59_mujeres: EditText
    private lateinit var edades_60_mas_hombres: EditText
    private lateinit var edades_60_mas_mujeres: EditText
    private lateinit var escuela_asistencia: EditText
    private lateinit var escolaridad_niveles: EditText
    private lateinit var familiares_enfermedad: EditText
    private lateinit var familiares_enfermedad_cuantos: EditText
    private lateinit var familiares_enfermedad_quien: EditText
    private lateinit var comprobante_enfermedad: EditText
    private lateinit var tratamiento_recibido: EditText
    private lateinit var tratamiento_lugar: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte7)

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
        familia_integrantes = findViewById(R.id.familia_integrantes)
        familia_total_ocupantes = findViewById(R.id.familia_total_ocupantes)
        familia_tipo = findViewById(R.id.familia_tipo)
        edades_0_5_hombres = findViewById(R.id.edades_0_5_hombres)
        edades_0_5_mujeres = findViewById(R.id.edades_0_5_mujeres)
        edades_6_12_hombres = findViewById(R.id.edades_6_12_hombres)
        edades_6_12_mujeres = findViewById(R.id.edades_6_12_mujeres)
        edades_13_18_hombres = findViewById(R.id.edades_13_18_hombres)
        edades_13_18_mujeres = findViewById(R.id.edades_13_18_mujeres)
        edades_19_35_hombres = findViewById(R.id.edades_19_35_hombres)
        edades_19_35_mujeres = findViewById(R.id.edades_19_35_mujeres)
        edades_36_59_hombres = findViewById(R.id.edades_36_59_hombres)
        edades_36_59_mujeres = findViewById(R.id.edades_36_59_mujeres)
        edades_60_mas_hombres = findViewById(R.id.edades_60_mas_hombres)
        edades_60_mas_mujeres = findViewById(R.id.edades_60_mas_mujeres)
        escuela_asistencia = findViewById(R.id.escuela_asistencia)
        escolaridad_niveles = findViewById(R.id.escolaridad_niveles)
        familiares_enfermedad = findViewById(R.id.familiares_enfermedad)
        familiares_enfermedad_cuantos = findViewById(R.id.familiares_enfermedad_cuantos)
        familiares_enfermedad_quien = findViewById(R.id.familiares_enfermedad_quien)
        comprobante_enfermedad = findViewById(R.id.comprobante_enfermedad)
        tratamiento_recibido = findViewById(R.id.tratamiento_recibido)
        tratamiento_lugar = findViewById(R.id.tratamiento_lugar)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Campos obligatorios principales
        val camposObligatorios = listOf(
            Pair(familia_integrantes, "Integrantes de la familia"),
            Pair(familia_total_ocupantes, "Total de ocupantes"),
            Pair(familia_tipo, "Tipo de familia"),
            Pair(escuela_asistencia, "Asistencia a la escuela"),
            Pair(escolaridad_niveles, "Niveles de escolaridad"),
            Pair(familiares_enfermedad, "Familiares con enfermedad")
        )

        for ((campo, nombre) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, complete el campo: $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar que los campos numéricos contengan números
        val camposNumericos = listOf(
            Pair(familia_integrantes, "Integrantes de la familia"),
            Pair(familia_total_ocupantes, "Total de ocupantes"),
            Pair(edades_0_5_hombres, "Edades 0-5 hombres"),
            Pair(edades_0_5_mujeres, "Edades 0-5 mujeres"),
            Pair(edades_6_12_hombres, "Edades 6-12 hombres"),
            Pair(edades_6_12_mujeres, "Edades 6-12 mujeres"),
            Pair(edades_13_18_hombres, "Edades 13-18 hombres"),
            Pair(edades_13_18_mujeres, "Edades 13-18 mujeres"),
            Pair(edades_19_35_hombres, "Edades 19-35 hombres"),
            Pair(edades_19_35_mujeres, "Edades 19-35 mujeres"),
            Pair(edades_36_59_hombres, "Edades 36-59 hombres"),
            Pair(edades_36_59_mujeres, "Edades 36-59 mujeres"),
            Pair(edades_60_mas_hombres, "Edades 60+ hombres"),
            Pair(edades_60_mas_mujeres, "Edades 60+ mujeres"),
            Pair(familiares_enfermedad_cuantos, "Cantidad de familiares enfermos")
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().isNotBlank() && campo.text.toString().toIntOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El campo '$nombre' debe ser un número entero",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación condicional si hay familiares enfermos
        if (familiares_enfermedad.text.toString().equals("Sí", ignoreCase = true)) {
            if (familiares_enfermedad_cuantos.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar cuántos familiares tienen enfermedad",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                familiares_enfermedad_cuantos.requestFocus()
                return false
            }

            if (familiares_enfermedad_quien.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar qué familiares tienen enfermedad",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                familiares_enfermedad_quien.requestFocus()
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
        val datos = datosFamiliares(
            familia_integrantes = familia_integrantes.text.toString(),
            familia_total_ocupantes = familia_total_ocupantes.text.toString(),
            familia_tipo = familia_tipo.text.toString(),
            edad_0_5_hombres = edades_0_5_hombres.text.toString(),
            edad_0_5_mujeres = edades_0_5_mujeres.text.toString(),
            edad_6_12_hombres = edades_6_12_hombres.text.toString(),
            edad_6_12_mujeres = edades_6_12_mujeres.text.toString(),
            edad_13_18_hombres = edades_13_18_hombres.text.toString(),
            edad_13_18_mujeres = edades_13_18_mujeres.text.toString(),
            edad_19_35_hombres = edades_19_35_hombres.text.toString(),
            edad_19_35_mujeres = edades_19_35_mujeres.text.toString(),
            edad_36_59_hombres = edades_36_59_hombres.text.toString(),
            edad_36_59_mujeres = edades_36_59_mujeres.text.toString(),
            edad_60_mas_hombres = edades_60_mas_hombres.text.toString(),
            edad_60_mas_mujeres = edades_60_mas_mujeres.text.toString(),
            escuela_asistencia = escuela_asistencia.text.toString(),
            escolaridad_niveles = escolaridad_niveles.text.toString(),
            familiares_enfermedad = familiares_enfermedad.text.toString(),
            familiares_enfermedad_cuantos = familiares_enfermedad_cuantos.text.toString(),
            familiares_enfermedad_quien = familiares_enfermedad_quien.text.toString(),
            comprobante_enfermedad = comprobante_enfermedad.text.toString(),
            tratamiento_recibido = tratamiento_recibido.text.toString(),
            tratamiento_lugar = tratamiento_lugar.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos familiares guardados correctamente",
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
        val intent = Intent(this, Formatoparte8Activity::class.java)
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