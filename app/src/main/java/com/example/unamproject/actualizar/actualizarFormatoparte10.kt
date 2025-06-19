package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosOtrosFamiliares
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte10 : AppCompatActivity() {

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

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte10)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupButtons()

        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        cargarDatosOtrosFamiliares()
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

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
            if (validarFormulario()) {
                mostrarConfirmacionGuardado()
            }
        }

        btnSiguiente.setOnClickListener {
            if (registroExistente) {
                irASiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarDatos() }
                )
            }
        }
    }

    private fun cargarDatosOtrosFamiliares() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosOtrosFamiliares(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        otrosHabitantesActividad.setText(datos.otros_habitantes_actividad)
                        hijoNumero.setText(datos.hijo_numero)
                        hijoActividad.setText(datos.hijo_actividad)
                        hijoAportacion.setText(datos.hijo_aportacion)
                        padreNumero.setText(datos.padre_numero)
                        padreActividad.setText(datos.padre_actividad)
                        padreAportacion.setText(datos.padre_aportacion)
                        madreNumero.setText(datos.madre_numero)
                        madreActividad.setText(datos.madre_actividad)
                        madreAportacion.setText(datos.madre_aportacion)
                        suegrosNumero.setText(datos.suegros_numero)
                        suegrosActividad.setText(datos.suegros_actividad)
                        suegrosAportacion.setText(datos.suegros_aportacion)
                        hermanosNumero.setText(datos.hermanos_numero)
                        hermanosActividad.setText(datos.hermanos_actividad)
                        hermanosAportacion.setText(datos.hermanos_aportacion)
                        nietosNumeros.setText(datos.nietos_numeros)
                        nietosActividad.setText(datos.nietos_actividad)
                        nietosAportacion.setText(datos.nietos_aportacion)
                        yernosNuerasNumero.setText(datos.yernos_nueras_numero)
                        yernosNuerasActividad.setText(datos.yernos_nueras_actividad)
                        yernosNuerasAportacion.setText(datos.yernos_nueras_aportacion)
                        otrosFamiliaresNumero.setText(datos.otros_familiares_numero)
                        otrosFamiliaresActividad.setText(datos.otros_familiares_actividad)
                        otrosFamiliaresAportacion.setText(datos.otros_familiares_aportacion)
                        noFamiliaresNumero.setText(datos.no_familiares_numero)
                        noFamiliaresActividad.setText(datos.no_familiares_actividad)
                        noFamiliaresAportacion.setText(datos.no_familiares_aportacion)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de otros familiares. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun validarFormulario(): Boolean {
        // Validar campos numéricos
        val camposNumericos = listOf(
            hijoNumero to "Número de hijos",
            padreNumero to "Número de padres",
            madreNumero to "Número de madres",
            suegrosNumero to "Número de suegros",
            hermanosNumero to "Número de hermanos",
            nietosNumeros to "Número de nietos",
            yernosNuerasNumero to "Número de yernos/nueras",
            otrosFamiliaresNumero to "Número de otros familiares",
            noFamiliaresNumero to "Número de no familiares"
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().isNotEmpty() && !campo.text.toString().matches(Regex("^\\d+\$"))) {
                campo.error = "Debe ser un número válido"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "El campo $nombre debe contener solo números enteros",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar grupos de campos (número + actividad + aportación)
        val gruposCampos = listOf(
            Triple(hijoNumero, hijoActividad, hijoAportacion) to "hijos",
            Triple(padreNumero, padreActividad, padreAportacion) to "padres",
            Triple(madreNumero, madreActividad, madreAportacion) to "madres",
            Triple(suegrosNumero, suegrosActividad, suegrosAportacion) to "suegros",
            Triple(hermanosNumero, hermanosActividad, hermanosAportacion) to "hermanos",
            Triple(nietosNumeros, nietosActividad, nietosAportacion) to "nietos",
            Triple(yernosNuerasNumero, yernosNuerasActividad, yernosNuerasAportacion) to "yernos/nueras",
            Triple(otrosFamiliaresNumero, otrosFamiliaresActividad, otrosFamiliaresAportacion) to "otros familiares",
            Triple(noFamiliaresNumero, noFamiliaresActividad, noFamiliaresAportacion) to "no familiares"
        )

        for ((grupo, nombreGrupo) in gruposCampos) {
            val (numero, actividad, aportacion) = grupo
            if (numero.text.toString().isNotEmpty() && (actividad.text.toString().isEmpty() || aportacion.text.toString().isEmpty())) {
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "Si especifica número de $nombreGrupo, debe completar tanto la actividad como la aportación",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                if (actividad.text.toString().isEmpty()) actividad.error = "Requerido"
                if (aportacion.text.toString().isEmpty()) aportacion.error = "Requerido"
                return false
            }
        }

        return true
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar los datos?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatos() }
        )
    }

    private fun guardarOActualizarDatos() {
        val datos = datosOtrosFamiliares(
            otros_habitantes_actividad = otrosHabitantesActividad.text.toString().trim(),
            hijo_numero = hijoNumero.text.toString().trim(),
            hijo_actividad = hijoActividad.text.toString().trim(),
            hijo_aportacion = hijoAportacion.text.toString().trim(),
            padre_numero = padreNumero.text.toString().trim(),
            padre_actividad = padreActividad.text.toString().trim(),
            padre_aportacion = padreAportacion.text.toString().trim(),
            madre_numero = madreNumero.text.toString().trim(),
            madre_actividad = madreActividad.text.toString().trim(),
            madre_aportacion = madreAportacion.text.toString().trim(),
            suegros_numero = suegrosNumero.text.toString().trim(),
            suegros_actividad = suegrosActividad.text.toString().trim(),
            suegros_aportacion = suegrosAportacion.text.toString().trim(),
            hermanos_numero = hermanosNumero.text.toString().trim(),
            hermanos_actividad = hermanosActividad.text.toString().trim(),
            hermanos_aportacion = hermanosAportacion.text.toString().trim(),
            nietos_numeros = nietosNumeros.text.toString().trim(),
            nietos_actividad = nietosActividad.text.toString().trim(),
            nietos_aportacion = nietosAportacion.text.toString().trim(),
            yernos_nueras_numero = yernosNuerasNumero.text.toString().trim(),
            yernos_nueras_actividad = yernosNuerasActividad.text.toString().trim(),
            yernos_nueras_aportacion = yernosNuerasAportacion.text.toString().trim(),
            otros_familiares_numero = otrosFamiliaresNumero.text.toString().trim(),
            otros_familiares_actividad = otrosFamiliaresActividad.text.toString().trim(),
            otros_familiares_aportacion = otrosFamiliaresAportacion.text.toString().trim(),
            no_familiares_numero = noFamiliaresNumero.text.toString().trim(),
            no_familiares_actividad = noFamiliaresActividad.text.toString().trim(),
            no_familiares_aportacion = noFamiliaresAportacion.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosOtrosFamiliares(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosOtrosFamiliares(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos de otros familiares actualizados correctamente"
                                else
                                    "Datos de otros familiares guardados correctamente",
                                iconoResId = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF388E3C.toInt()
                            )
                        } else {
                            mostrarErrorServidor("El servidor no pudo procesar la solicitud")
                        }
                    } ?: mostrarErrorServidor("Respuesta vacía del servidor")
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun manejarErrorRespuesta(codigo: Int, mensajeError: String?) {
        when (codigo) {
            404 -> mostrarDialogo(
                titulo = "No encontrado",
                mensaje = "El registro no fue encontrado",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            400 -> mostrarDialogo(
                titulo = "Datos inválidos",
                mensaje = "Verifique la información ingresada: ${mensajeError ?: "Error 400"}",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            500 -> mostrarErrorServidor("Error interno del servidor: ${mensajeError ?: "sin detalles"}")
            else -> mostrarErrorServidor("Error $codigo: ${mensajeError ?: "Error desconocido"}")
        }
    }

    private fun mostrarErrorServidor(mensaje: String) {
        mostrarDialogo(
            titulo = "Error del servidor",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorConexion(mensaje: String) {
        mostrarDialogo(
            titulo = "Error de conexión",
            mensaje = "No se pudo conectar al servidor: $mensaje",
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = "Ocurrió un error: $mensaje",
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

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte11::class.java).apply {
            putExtra("id_acreditado", idAcreditado)
            putExtra("id_usuario", idUsuario)
        }
        startActivity(intent)
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