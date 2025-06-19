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

class Formatoparte7SinConexion : AppCompatActivity() {

    private lateinit var integrantesEt: EditText
    private lateinit var totalOcupantesEt: EditText
    private lateinit var tipoFamiliaEt: EditText
    private lateinit var edad0_5HombresEt: EditText
    private lateinit var edad0_5MujeresEt: EditText
    private lateinit var edad6_12HombresEt: EditText
    private lateinit var edad6_12MujeresEt: EditText
    private lateinit var edad13_18HombresEt: EditText
    private lateinit var edad13_18MujeresEt: EditText
    private lateinit var edad19_35HombresEt: EditText
    private lateinit var edad19_35MujeresEt: EditText
    private lateinit var edad36_59HombresEt: EditText
    private lateinit var edad36_59MujeresEt: EditText
    private lateinit var edad60MasHombresEt: EditText
    private lateinit var edad60MasMujeresEt: EditText
    private lateinit var escuelaAsistenciaEt: EditText
    private lateinit var escolaridadNivelesEt: EditText
    private lateinit var familiaresEnfermedadEt: EditText
    private lateinit var familiaresEnfermedadCuantosEt: EditText
    private lateinit var familiaresEnfermedadQuienEt: EditText
    private lateinit var comprobanteEnfermedadEt: EditText
    private lateinit var tratamientoRecibidoEt: EditText
    private lateinit var tratamientoLugarEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte7_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        // Inicializar base de datos
        database = AppDatabase.getDatabase(this)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        integrantesEt = findViewById(R.id.familia_integrantes)
        totalOcupantesEt = findViewById(R.id.familia_total_ocupantes)
        tipoFamiliaEt = findViewById(R.id.familia_tipo)
        edad0_5HombresEt = findViewById(R.id.edades_0_5_hombres)
        edad0_5MujeresEt = findViewById(R.id.edades_0_5_mujeres)
        edad6_12HombresEt = findViewById(R.id.edades_6_12_hombres)
        edad6_12MujeresEt = findViewById(R.id.edades_6_12_mujeres)
        edad13_18HombresEt = findViewById(R.id.edades_13_18_hombres)
        edad13_18MujeresEt = findViewById(R.id.edades_13_18_mujeres)
        edad19_35HombresEt = findViewById(R.id.edades_19_35_hombres)
        edad19_35MujeresEt = findViewById(R.id.edades_19_35_mujeres)
        edad36_59HombresEt = findViewById(R.id.edades_36_59_hombres)
        edad36_59MujeresEt = findViewById(R.id.edades_36_59_mujeres)
        edad60MasHombresEt = findViewById(R.id.edades_60_mas_hombres)
        edad60MasMujeresEt = findViewById(R.id.edades_60_mas_mujeres)
        escuelaAsistenciaEt = findViewById(R.id.escuela_asistencia)
        escolaridadNivelesEt = findViewById(R.id.escolaridad_niveles)
        familiaresEnfermedadEt = findViewById(R.id.familiares_enfermedad)
        familiaresEnfermedadCuantosEt = findViewById(R.id.familiares_enfermedad_cuantos)
        familiaresEnfermedadQuienEt = findViewById(R.id.familiares_enfermedad_quien)
        comprobanteEnfermedadEt = findViewById(R.id.comprobante_enfermedad)
        tratamientoRecibidoEt = findViewById(R.id.tratamiento_recibido)
        tratamientoLugarEt = findViewById(R.id.tratamiento_lugar)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosFamiliares()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosFamiliares() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        val camposRequeridos = listOf(
            integrantesEt to "Número de integrantes de la familia",
            totalOcupantesEt to "Total de ocupantes",
            tipoFamiliaEt to "Tipo de familia"
        )

        for ((campo, nombre) in camposRequeridos) {
            if (campo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "El campo $nombre es obligatorio",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar que los campos de edades sean numéricos
        val camposEdades = listOf(
            edad0_5HombresEt to "Hombres 0-5 años",
            edad0_5MujeresEt to "Mujeres 0-5 años",
            edad6_12HombresEt to "Hombres 6-12 años",
            edad6_12MujeresEt to "Mujeres 6-12 años",
            edad13_18HombresEt to "Hombres 13-18 años",
            edad13_18MujeresEt to "Mujeres 13-18 años",
            edad19_35HombresEt to "Hombres 19-35 años",
            edad19_35MujeresEt to "Mujeres 19-35 años",
            edad36_59HombresEt to "Hombres 36-59 años",
            edad36_59MujeresEt to "Mujeres 36-59 años",
            edad60MasHombresEt to "Hombres 60+ años",
            edad60MasMujeresEt to "Mujeres 60+ años"
        )

        for ((campo, nombre) in camposEdades) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty()) {
                try {
                    valor.toInt()
                } catch (e: NumberFormatException) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "El campo $nombre debe ser un número entero",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar que si hay familiares con enfermedad, se especifique cuántos y quiénes
        if (familiaresEnfermedadEt.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (familiaresEnfermedadCuantosEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar cuántos familiares tienen enfermedad",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                familiaresEnfermedadCuantosEt.requestFocus()
                return false
            }

            if (familiaresEnfermedadQuienEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar qué familiares tienen enfermedad",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                familiaresEnfermedadQuienEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarDatosFamiliares() {
        val datos = DatosFamiliaresEntity(
            familia_integrantes = integrantesEt.text.toString().trim(),
            familia_total_ocupantes = totalOcupantesEt.text.toString().trim(),
            familia_tipo = tipoFamiliaEt.text.toString().trim(),
            edad_0_5_hombres = edad0_5HombresEt.text.toString().trim(),
            edad_0_5_mujeres = edad0_5MujeresEt.text.toString().trim(),
            edad_6_12_hombres = edad6_12HombresEt.text.toString().trim(),
            edad_6_12_mujeres = edad6_12MujeresEt.text.toString().trim(),
            edad_13_18_hombres = edad13_18HombresEt.text.toString().trim(),
            edad_13_18_mujeres = edad13_18MujeresEt.text.toString().trim(),
            edad_19_35_hombres = edad19_35HombresEt.text.toString().trim(),
            edad_19_35_mujeres = edad19_35MujeresEt.text.toString().trim(),
            edad_36_59_hombres = edad36_59HombresEt.text.toString().trim(),
            edad_36_59_mujeres = edad36_59MujeresEt.text.toString().trim(),
            edad_60_mas_hombres = edad60MasHombresEt.text.toString().trim(),
            edad_60_mas_mujeres = edad60MasMujeresEt.text.toString().trim(),
            escuela_asistencia = escuelaAsistenciaEt.text.toString().trim(),
            escolaridad_niveles = escolaridadNivelesEt.text.toString().trim(),
            familiares_enfermedad = familiaresEnfermedadEt.text.toString().trim(),
            familiares_enfermedad_cuantos = familiaresEnfermedadCuantosEt.text.toString().trim(),
            familiares_enfermedad_quien = familiaresEnfermedadQuienEt.text.toString().trim(),
            comprobante_enfermedad = comprobanteEnfermedadEt.text.toString().trim(),
            tratamiento_recibido = tratamientoRecibidoEt.text.toString().trim(),
            tratamiento_lugar = tratamientoLugarEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosFamiliaresDao().insertDatosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos familiares guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos familiares: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte8SinConexion::class.java)
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