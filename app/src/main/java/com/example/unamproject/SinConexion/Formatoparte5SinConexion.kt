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

class Formatoparte5SinConexion : AppCompatActivity() {

    private lateinit var motivoEt: EditText
    private lateinit var documentoEt: EditText
    private lateinit var tipoDocumentoEt: EditText
    private lateinit var solicitanteEsAcredEt: EditText
    private lateinit var solicitanteNombreEt: EditText
    private lateinit var parentescoEt: EditText
    private lateinit var motivoPersonalEt: EditText
    private lateinit var sexoEt: EditText
    private lateinit var fechaNacimientoEt: EditText
    private lateinit var edadEt: EditText
    private lateinit var lugarNacimientoEt: EditText
    private lateinit var gradoEstudiosEt: EditText
    private lateinit var conocimientoCompEt: EditText
    private lateinit var discapacidadEt: EditText
    private lateinit var dictamenEt: EditText
    private lateinit var institucionDictamenEt: EditText
    private lateinit var fechaDictamenEt: EditText
    private lateinit var porcentajeDiscapacidadEt: EditText
    private lateinit var estadoCivilEt: EditText
    private lateinit var fechaEstadoCivilEt: EditText
    private lateinit var exesposoAportacionEt: EditText
    private lateinit var exesposoMontoEt: EditText
    private lateinit var regimenConyugalEt: EditText
    private lateinit var viveConConyugeEt: EditText
    private lateinit var fechaNoConviveEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte5_sin_conexion)

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
        motivoEt = findViewById(R.id.reestructura_motivo)
        documentoEt = findViewById(R.id.reestructura_documento)
        tipoDocumentoEt = findViewById(R.id.reestructura_tipo_documento)
        solicitanteEsAcredEt = findViewById(R.id.reestructura_solicitante_es_acred)
        solicitanteNombreEt = findViewById(R.id.reestructura_solicitante_nombre)
        parentescoEt = findViewById(R.id.reestructura_parentesco)
        motivoPersonalEt = findViewById(R.id.reestructura_motivo_personal)
        sexoEt = findViewById(R.id.reestructura_sexo)
        fechaNacimientoEt = findViewById(R.id.reestructura_fecha_nacimiento)
        edadEt = findViewById(R.id.reestructura_edad)
        lugarNacimientoEt = findViewById(R.id.reestructura_lugar_nacimiento)
        gradoEstudiosEt = findViewById(R.id.reestructura_grado_estudios)
        conocimientoCompEt = findViewById(R.id.reestructura_conocimiento_comp)
        discapacidadEt = findViewById(R.id.reestructura_discapacidad)
        dictamenEt = findViewById(R.id.reestructura_dictamen)
        institucionDictamenEt = findViewById(R.id.reestructura_institucion_dictamen)
        fechaDictamenEt = findViewById(R.id.reestructura_fecha_dictamen)
        porcentajeDiscapacidadEt = findViewById(R.id.reestructura_porcentaje_discapacidad)
        estadoCivilEt = findViewById(R.id.reestructura_estado_civil)
        fechaEstadoCivilEt = findViewById(R.id.reestructura_fecha_estado_civil)
        exesposoAportacionEt = findViewById(R.id.reestructura_exesposo_aportacion)
        exesposoMontoEt = findViewById(R.id.reestructura_exesposo_monto)
        regimenConyugalEt = findViewById(R.id.reestructura_regimen_conyugal)
        viveConConyugeEt = findViewById(R.id.reestructura_vive_con_conyuge)
        fechaNoConviveEt = findViewById(R.id.reestructura_fecha_no_convive)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosReestructura()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosReestructura() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        val camposRequeridos = listOf(
            motivoEt to "Motivo de reestructura",
            documentoEt to "Documento",
            solicitanteEsAcredEt to "¿El solicitante es el acreditado?",
            sexoEt to "Sexo",
            fechaNacimientoEt to "Fecha de nacimiento",
            edadEt to "Edad",
            estadoCivilEt to "Estado civil"
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

        // Validar formatos de fecha
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        val fechas = listOf(
            fechaNacimientoEt to "Fecha de nacimiento",
            fechaDictamenEt to "Fecha de dictamen",
            fechaEstadoCivilEt to "Fecha de estado civil",
            fechaNoConviveEt to "Fecha de no convivencia"
        )

        for ((campo, nombre) in fechas) {
            val fechaStr = campo.text.toString().trim()
            if (fechaStr.isNotEmpty()) {
                try {
                    dateFormat.parse(fechaStr)
                } catch (e: Exception) {
                    mostrarDialogo(
                        titulo = "Formato inválido",
                        mensaje = "$nombre: debe tener formato dd/MM/yyyy",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar valores numéricos
        val numericos = listOf(
            edadEt to "Edad",
            porcentajeDiscapacidadEt to "Porcentaje de discapacidad",
            exesposoMontoEt to "Monto de aportación del exesposo"
        )

        for ((campo, nombre) in numericos) {
            val valorStr = campo.text.toString().trim()
            if (valorStr.isNotEmpty()) {
                try {
                    valorStr.toInt()
                } catch (e: NumberFormatException) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "$nombre: debe ser un valor numérico entero",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar edad razonable (1-120 años)
        val edad = edadEt.text.toString().trim().toIntOrNull() ?: 0
        if (edad < 1 || edad > 120) {
            mostrarDialogo(
                titulo = "Edad inválida",
                mensaje = "La edad debe estar entre 1 y 120 años",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            edadEt.requestFocus()
            return false
        }

        // Validar porcentaje de discapacidad (0-100)
        porcentajeDiscapacidadEt.text.toString().trim().toIntOrNull()?.let { porcentaje ->
            if (porcentaje < 0 || porcentaje > 100) {
                mostrarDialogo(
                    titulo = "Porcentaje inválido",
                    mensaje = "El porcentaje de discapacidad debe estar entre 0 y 100",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                porcentajeDiscapacidadEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarDatosReestructura() {
        val datos = DatosReestructuraEntity(
            reestructura_motivo = motivoEt.text.toString().trim(),
            reestructura_documento = documentoEt.text.toString().trim(),
            reestructura_tipo_documento = tipoDocumentoEt.text.toString().trim(),
            reestructura_solicitante_es_acred = solicitanteEsAcredEt.text.toString().trim(),
            reestructura_solicitante_nombre = solicitanteNombreEt.text.toString().trim(),
            reestructura_parentesco = parentescoEt.text.toString().trim(),
            reestructura_motivo_personal = motivoPersonalEt.text.toString().trim(),
            reestructura_sexo = sexoEt.text.toString().trim(),
            reestructura_fecha_nacimiento = fechaNacimientoEt.text.toString().trim(),
            reestructura_edad = edadEt.text.toString().trim(),
            reestructura_lugar_nacimiento = lugarNacimientoEt.text.toString().trim(),
            reestructura_grado_estudios = gradoEstudiosEt.text.toString().trim(),
            reestructura_conocimiento_comp = conocimientoCompEt.text.toString().trim(),
            reestructura_discapacidad = discapacidadEt.text.toString().trim(),
            reestructura_dictamen = dictamenEt.text.toString().trim(),
            reestructura_institucion_dictamen = institucionDictamenEt.text.toString().trim(),
            reestructura_fecha_dictamen = fechaDictamenEt.text.toString().trim(),
            reestructura_porcentaje_discapacidad = porcentajeDiscapacidadEt.text.toString().trim(),
            reestructura_estado_civil = estadoCivilEt.text.toString().trim(),
            reestructura_fecha_estado_civil = fechaEstadoCivilEt.text.toString().trim(),
            reestructura_exesposo_aportacion = exesposoAportacionEt.text.toString().trim(),
            reestructura_exesposo_monto = exesposoMontoEt.text.toString().trim(),
            reestructura_regimen_conyugal = regimenConyugalEt.text.toString().trim(),
            reestructura_vive_con_conyuge = viveConConyugeEt.text.toString().trim(),
            reestructura_fecha_no_convive = fechaNoConviveEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosReestructuraDao().insertDatosReestructura(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de reestructura guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte6SinConexion::class.java)
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