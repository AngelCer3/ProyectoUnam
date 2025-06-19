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

class Formatoparte10SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts
    private lateinit var otrosHabitantesActividadEt: EditText
    private lateinit var hijoNumeroEt: EditText
    private lateinit var hijoActividadEt: EditText
    private lateinit var hijoAportacionEt: EditText
    private lateinit var padreNumeroEt: EditText
    private lateinit var padreActividadEt: EditText
    private lateinit var padreAportacionEt: EditText
    private lateinit var madreNumeroEt: EditText
    private lateinit var madreActividadEt: EditText
    private lateinit var madreAportacionEt: EditText
    private lateinit var suegrosNumeroEt: EditText
    private lateinit var suegrosActividadEt: EditText
    private lateinit var suegrosAportacionEt: EditText
    private lateinit var hermanosNumeroEt: EditText
    private lateinit var hermanosActividadEt: EditText
    private lateinit var hermanosAportacionEt: EditText
    private lateinit var nietosNumerosEt: EditText
    private lateinit var nietosActividadEt: EditText
    private lateinit var nietosAportacionEt: EditText
    private lateinit var yernosNuerasNumeroEt: EditText
    private lateinit var yernosNuerasActividadEt: EditText
    private lateinit var yernosNuerasAportacionEt: EditText
    private lateinit var otrosFamiliaresNumeroEt: EditText
    private lateinit var otrosFamiliaresActividadEt: EditText
    private lateinit var otrosFamiliaresAportacionEt: EditText
    private lateinit var noFamiliaresNumeroEt: EditText
    private lateinit var noFamiliaresActividadEt: EditText
    private lateinit var noFamiliaresAportacionEt: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte10_sin_conexion)

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
        // Inicializar EditTexts
        otrosHabitantesActividadEt = findViewById(R.id.otros_habitantes_actividad)
        hijoNumeroEt = findViewById(R.id.hijo_numero)
        hijoActividadEt = findViewById(R.id.hijo_actividad)
        hijoAportacionEt = findViewById(R.id.hijo_aportacion)
        padreNumeroEt = findViewById(R.id.padre_numero)
        padreActividadEt = findViewById(R.id.padre_actividad)
        padreAportacionEt = findViewById(R.id.padre_aportacion)
        madreNumeroEt = findViewById(R.id.madre_numero)
        madreActividadEt = findViewById(R.id.madre_actividad)
        madreAportacionEt = findViewById(R.id.madre_aportacion)
        suegrosNumeroEt = findViewById(R.id.suegros_numero)
        suegrosActividadEt = findViewById(R.id.suegros_actividad)
        suegrosAportacionEt = findViewById(R.id.suegros_aportacion)
        hermanosNumeroEt = findViewById(R.id.hermanos_numero)
        hermanosActividadEt = findViewById(R.id.hermanos_actividad)
        hermanosAportacionEt = findViewById(R.id.hermanos_aportacion)
        nietosNumerosEt = findViewById(R.id.nietos_numeros)
        nietosActividadEt = findViewById(R.id.nietos_actividad)
        nietosAportacionEt = findViewById(R.id.nietos_aportacion)
        yernosNuerasNumeroEt = findViewById(R.id.yernos_nueras_numero)
        yernosNuerasActividadEt = findViewById(R.id.yernos_nueras_actividad)
        yernosNuerasAportacionEt = findViewById(R.id.yernos_nueras_aportacion)
        otrosFamiliaresNumeroEt = findViewById(R.id.otros_familiares_numero)
        otrosFamiliaresActividadEt = findViewById(R.id.otros_familiares_actividad)
        otrosFamiliaresAportacionEt = findViewById(R.id.otros_familiares_aportacion)
        noFamiliaresNumeroEt = findViewById(R.id.no_familiares_numero)
        noFamiliaresActividadEt = findViewById(R.id.no_familiares_actividad)
        noFamiliaresAportacionEt = findViewById(R.id.no_familiares_aportacion)

        // Botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosOtrosFamiliares()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosOtrosFamiliares() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar que los campos numéricos contengan números válidos
        val camposNumericos = listOf(
            hijoNumeroEt to "Número de hijos",
            padreNumeroEt to "Número de padres",
            madreNumeroEt to "Número de madres",
            suegrosNumeroEt to "Número de suegros",
            hermanosNumeroEt to "Número de hermanos",
            nietosNumerosEt to "Número de nietos",
            yernosNuerasNumeroEt to "Número de yernos/nueras",
            otrosFamiliaresNumeroEt to "Número de otros familiares",
            noFamiliaresNumeroEt to "Número de no familiares"
        )

        for ((campo, nombre) in camposNumericos) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty()) {
                try {
                    val num = valor.toInt()
                    if (num < 0) {
                        mostrarDialogo(
                            titulo = "Valor inválido",
                            mensaje = "El campo $nombre no puede ser negativo",
                            iconoResId = android.R.drawable.ic_dialog_alert,
                            colorTitulo = 0xFFD32F2F.toInt()
                        )
                        campo.requestFocus()
                        return false
                    }
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

        // Validar que si hay número de familiares, se especifique actividad y aportación
        val gruposFamiliares = listOf(
            Triple(hijoNumeroEt, hijoActividadEt, hijoAportacionEt) to "hijos",
            Triple(padreNumeroEt, padreActividadEt, padreAportacionEt) to "padres",
            Triple(madreNumeroEt, madreActividadEt, madreAportacionEt) to "madres",
            Triple(suegrosNumeroEt, suegrosActividadEt, suegrosAportacionEt) to "suegros",
            Triple(hermanosNumeroEt, hermanosActividadEt, hermanosAportacionEt) to "hermanos",
            Triple(nietosNumerosEt, nietosActividadEt, nietosAportacionEt) to "nietos",
            Triple(yernosNuerasNumeroEt, yernosNuerasActividadEt, yernosNuerasAportacionEt) to "yernos/nueras",
            Triple(otrosFamiliaresNumeroEt, otrosFamiliaresActividadEt, otrosFamiliaresAportacionEt) to "otros familiares",
            Triple(noFamiliaresNumeroEt, noFamiliaresActividadEt, noFamiliaresAportacionEt) to "no familiares"
        )

        for ((grupo, nombreGrupo) in gruposFamiliares) {
            val (numeroEt, actividadEt, aportacionEt) = grupo
            if (numeroEt.text.toString().trim().toIntOrNull() ?: 0 > 0) {
                if (actividadEt.text.toString().trim().isEmpty()) {
                    mostrarDialogo(
                        titulo = "Campo requerido",
                        mensaje = "Debe especificar la actividad de los $nombreGrupo",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    actividadEt.requestFocus()
                    return false
                }

                if (aportacionEt.text.toString().trim().isEmpty()) {
                    mostrarDialogo(
                        titulo = "Campo requerido",
                        mensaje = "Debe especificar la aportación de los $nombreGrupo",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    aportacionEt.requestFocus()
                    return false
                }
            }
        }

        return true
    }

    private fun guardarDatosOtrosFamiliares() {
        val datos = DatosOtrosFamiliaresEntity(
            otros_habitantes_actividad = otrosHabitantesActividadEt.text.toString().trim(),
            hijo_numero = hijoNumeroEt.text.toString().trim(),
            hijo_actividad = hijoActividadEt.text.toString().trim(),
            hijo_aportacion = hijoAportacionEt.text.toString().trim(),
            padre_numero = padreNumeroEt.text.toString().trim(),
            padre_actividad = padreActividadEt.text.toString().trim(),
            padre_aportacion = padreAportacionEt.text.toString().trim(),
            madre_numero = madreNumeroEt.text.toString().trim(),
            madre_actividad = madreActividadEt.text.toString().trim(),
            madre_aportacion = madreAportacionEt.text.toString().trim(),
            suegros_numero = suegrosNumeroEt.text.toString().trim(),
            suegros_actividad = suegrosActividadEt.text.toString().trim(),
            suegros_aportacion = suegrosAportacionEt.text.toString().trim(),
            hermanos_numero = hermanosNumeroEt.text.toString().trim(),
            hermanos_actividad = hermanosActividadEt.text.toString().trim(),
            hermanos_aportacion = hermanosAportacionEt.text.toString().trim(),
            nietos_numeros = nietosNumerosEt.text.toString().trim(),
            nietos_actividad = nietosActividadEt.text.toString().trim(),
            nietos_aportacion = nietosAportacionEt.text.toString().trim(),
            yernos_nueras_numero = yernosNuerasNumeroEt.text.toString().trim(),
            yernos_nueras_actividad = yernosNuerasActividadEt.text.toString().trim(),
            yernos_nueras_aportacion = yernosNuerasAportacionEt.text.toString().trim(),
            otros_familiares_numero = otrosFamiliaresNumeroEt.text.toString().trim(),
            otros_familiares_actividad = otrosFamiliaresActividadEt.text.toString().trim(),
            otros_familiares_aportacion = otrosFamiliaresAportacionEt.text.toString().trim(),
            no_familiares_numero = noFamiliaresNumeroEt.text.toString().trim(),
            no_familiares_actividad = noFamiliaresActividadEt.text.toString().trim(),
            no_familiares_aportacion = noFamiliaresAportacionEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosOtrosFamiliaresDao().insertDatosOtrosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de familiares guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de familiares: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte11SinConexion::class.java)
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