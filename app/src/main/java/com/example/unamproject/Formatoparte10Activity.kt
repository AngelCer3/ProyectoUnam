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

class Formatoparte10Activity : AppCompatActivity() {

    private lateinit var otrosHabitantesActividad: EditText
    private lateinit var hijoNumero: EditText
    private lateinit var hijoActividad: EditText
    private lateinit var hijoAportacion: EditText
    private lateinit var padreNumero: EditText
    private lateinit var padreActividad: EditText
    private lateinit var padreAportacion: EditText
    private lateinit var madreNumero: EditText
    private lateinit var madreActividad: EditText
    private lateinit var madreAportacion: EditText
    private lateinit var suegrosNumero: EditText
    private lateinit var suegrosActividad: EditText
    private lateinit var suegrosAportacion: EditText
    private lateinit var hermanosNumero: EditText
    private lateinit var hermanosActividad: EditText
    private lateinit var hermanosAportacion: EditText
    private lateinit var nietosNumeros: EditText
    private lateinit var nietosActividad: EditText
    private lateinit var nietosAportacion: EditText
    private lateinit var yernosNuerasNumero: EditText
    private lateinit var yernosNuerasActividad: EditText
    private lateinit var yernosNuerasAportacion: EditText
    private lateinit var otrosFamiliaresNumero: EditText
    private lateinit var otrosFamiliaresActividad: EditText
    private lateinit var otrosFamiliaresAportacion: EditText
    private lateinit var noFamiliaresNumero: EditText
    private lateinit var noFamiliaresActividad: EditText
    private lateinit var noFamiliaresAportacion: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte10)
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
        otrosHabitantesActividad = findViewById(R.id.otros_habitantes_actividad)
        hijoNumero = findViewById(R.id.hijo_numero)
        hijoActividad = findViewById(R.id.hijo_actividad)
        hijoAportacion = findViewById(R.id.hijo_aportacion)
        padreNumero = findViewById(R.id.padre_numero)
        padreActividad = findViewById(R.id.padre_actividad)
        padreAportacion = findViewById(R.id.padre_aportacion)
        madreNumero = findViewById(R.id.madre_numero)
        madreActividad = findViewById(R.id.madre_actividad)
        madreAportacion = findViewById(R.id.madre_aportacion)
        suegrosNumero = findViewById(R.id.suegros_numero)
        suegrosActividad = findViewById(R.id.suegros_actividad)
        suegrosAportacion = findViewById(R.id.suegros_aportacion)
        hermanosNumero = findViewById(R.id.hermanos_numero)
        hermanosActividad = findViewById(R.id.hermanos_actividad)
        hermanosAportacion = findViewById(R.id.hermanos_aportacion)
        nietosNumeros = findViewById(R.id.nietos_numeros)
        nietosActividad = findViewById(R.id.nietos_actividad)
        nietosAportacion = findViewById(R.id.nietos_aportacion)
        yernosNuerasNumero = findViewById(R.id.yernos_nueras_numero)
        yernosNuerasActividad = findViewById(R.id.yernos_nueras_actividad)
        yernosNuerasAportacion = findViewById(R.id.yernos_nueras_aportacion)
        otrosFamiliaresNumero = findViewById(R.id.otros_familiares_numero)
        otrosFamiliaresActividad = findViewById(R.id.otros_familiares_actividad)
        otrosFamiliaresAportacion = findViewById(R.id.otros_familiares_aportacion)
        noFamiliaresNumero = findViewById(R.id.no_familiares_numero)
        noFamiliaresActividad = findViewById(R.id.no_familiares_actividad)
        noFamiliaresAportacion = findViewById(R.id.no_familiares_aportacion)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Validar campos numéricos para cantidades
        val camposNumericos = listOf(
            Pair(hijoNumero, "Número de hijos"),
            Pair(padreNumero, "Número de padres"),
            Pair(madreNumero, "Número de madres"),
            Pair(suegrosNumero, "Número de suegros"),
            Pair(hermanosNumero, "Número de hermanos"),
            Pair(nietosNumeros, "Número de nietos"),
            Pair(yernosNuerasNumero, "Número de yernos/nueras"),
            Pair(otrosFamiliaresNumero, "Número de otros familiares"),
            Pair(noFamiliaresNumero, "Número de no familiares")
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

        // Validar campos numéricos para aportaciones
        val camposAportaciones = listOf(
            Pair(hijoAportacion, "Aportación de hijos"),
            Pair(padreAportacion, "Aportación de padres"),
            Pair(madreAportacion, "Aportación de madres"),
            Pair(suegrosAportacion, "Aportación de suegros"),
            Pair(hermanosAportacion, "Aportación de hermanos"),
            Pair(nietosAportacion, "Aportación de nietos"),
            Pair(yernosNuerasAportacion, "Aportación de yernos/nueras"),
            Pair(otrosFamiliaresAportacion, "Aportación de otros familiares"),
            Pair(noFamiliaresAportacion, "Aportación de no familiares")
        )

        for ((campo, nombre) in camposAportaciones) {
            if (campo.text.toString().isNotBlank() && campo.text.toString().toDoubleOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El campo '$nombre' debe ser un valor monetario",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación condicional: si hay número > 0, actividad es obligatoria
        val gruposFamiliares = listOf(
            Triple(hijoNumero, hijoActividad, "hijos"),
            Triple(padreNumero, padreActividad, "padres"),
            Triple(madreNumero, madreActividad, "madres"),
            Triple(suegrosNumero, suegrosActividad, "suegros"),
            Triple(hermanosNumero, hermanosActividad, "hermanos"),
            Triple(nietosNumeros, nietosActividad, "nietos"),
            Triple(yernosNuerasNumero, yernosNuerasActividad, "yernos/nueras"),
            Triple(otrosFamiliaresNumero, otrosFamiliaresActividad, "otros familiares"),
            Triple(noFamiliaresNumero, noFamiliaresActividad, "no familiares")
        )

        for ((campoNumero, campoActividad, nombreGrupo) in gruposFamiliares) {
            val cantidad = campoNumero.text.toString().toIntOrNull() ?: 0
            if (cantidad > 0 && campoActividad.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la actividad de los $nombreGrupo",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campoActividad.requestFocus()
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
        val datos = datosOtrosFamiliares(
            otros_habitantes_actividad = otrosHabitantesActividad.text.toString(),
            hijo_numero = hijoNumero.text.toString(),
            hijo_actividad = hijoActividad.text.toString(),
            hijo_aportacion = hijoAportacion.text.toString(),
            padre_numero = padreNumero.text.toString(),
            padre_actividad = padreActividad.text.toString(),
            padre_aportacion = padreAportacion.text.toString(),
            madre_numero = madreNumero.text.toString(),
            madre_actividad = madreActividad.text.toString(),
            madre_aportacion = madreAportacion.text.toString(),
            suegros_numero = suegrosNumero.text.toString(),
            suegros_actividad = suegrosActividad.text.toString(),
            suegros_aportacion = suegrosAportacion.text.toString(),
            hermanos_numero = hermanosNumero.text.toString(),
            hermanos_actividad = hermanosActividad.text.toString(),
            hermanos_aportacion = hermanosAportacion.text.toString(),
            nietos_numeros = nietosNumeros.text.toString(),
            nietos_actividad = nietosActividad.text.toString(),
            nietos_aportacion = nietosAportacion.text.toString(),
            yernos_nueras_numero = yernosNuerasNumero.text.toString(),
            yernos_nueras_actividad = yernosNuerasActividad.text.toString(),
            yernos_nueras_aportacion = yernosNuerasAportacion.text.toString(),
            otros_familiares_numero = otrosFamiliaresNumero.text.toString(),
            otros_familiares_actividad = otrosFamiliaresActividad.text.toString(),
            otros_familiares_aportacion = otrosFamiliaresAportacion.text.toString(),
            no_familiares_numero = noFamiliaresNumero.text.toString(),
            no_familiares_actividad = noFamiliaresActividad.text.toString(),
            no_familiares_aportacion = noFamiliaresAportacion.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosOtrosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos de otros familiares guardados correctamente",
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
        val intent = Intent(this, Formatoparte11Activity::class.java)
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