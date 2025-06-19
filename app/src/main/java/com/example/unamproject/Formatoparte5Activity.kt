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

class Formatoparte5Activity : AppCompatActivity() {

    private lateinit var guardar: Button
    private lateinit var siguiente: Button

    private lateinit var motivo: EditText
    private lateinit var documento: EditText
    private lateinit var tipoDocumento: EditText
    private lateinit var solicitanteEsAcred: EditText
    private lateinit var solicitanteNombre: EditText
    private lateinit var parentesco: EditText
    private lateinit var motivoPersonal: EditText
    private lateinit var sexo: EditText
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var lugarNacimiento: EditText
    private lateinit var gradoEstudios: EditText
    private lateinit var conocimientoComp: EditText
    private lateinit var discapacidad: EditText
    private lateinit var dictamen: EditText
    private lateinit var institucionDictamen: EditText
    private lateinit var fechaDictamen: EditText
    private lateinit var porcentajeDiscapacidad: EditText
    private lateinit var estadoCivil: EditText
    private lateinit var fechaEstadoCivil: EditText
    private lateinit var exesposoAportacion: EditText
    private lateinit var exesposoMonto: EditText
    private lateinit var regimenConyugal: EditText
    private lateinit var viveConConyuge: EditText
    private lateinit var fechaNoConvive: EditText

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte5)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
            if (validarCampos()) {
                siguienteFormato()
            }
        }
    }

    private fun initViews() {
        motivo = findViewById(R.id.reestructura_motivo)
        documento = findViewById(R.id.reestructura_documento)
        tipoDocumento = findViewById(R.id.reestructura_tipo_documento)
        solicitanteEsAcred = findViewById(R.id.reestructura_solicitante_es_acred)
        solicitanteNombre = findViewById(R.id.reestructura_solicitante_nombre)
        parentesco = findViewById(R.id.reestructura_parentesco)
        motivoPersonal = findViewById(R.id.reestructura_motivo_personal)
        sexo = findViewById(R.id.reestructura_sexo)
        fechaNacimiento = findViewById(R.id.reestructura_fecha_nacimiento)
        edad = findViewById(R.id.reestructura_edad)
        lugarNacimiento = findViewById(R.id.reestructura_lugar_nacimiento)
        gradoEstudios = findViewById(R.id.reestructura_grado_estudios)
        conocimientoComp = findViewById(R.id.reestructura_conocimiento_comp)
        discapacidad = findViewById(R.id.reestructura_discapacidad)
        dictamen = findViewById(R.id.reestructura_dictamen)
        institucionDictamen = findViewById(R.id.reestructura_institucion_dictamen)
        fechaDictamen = findViewById(R.id.reestructura_fecha_dictamen)
        porcentajeDiscapacidad = findViewById(R.id.reestructura_porcentaje_discapacidad)
        estadoCivil = findViewById(R.id.reestructura_estado_civil)
        fechaEstadoCivil = findViewById(R.id.reestructura_fecha_estado_civil)
        exesposoAportacion = findViewById(R.id.reestructura_exesposo_aportacion)
        exesposoMonto = findViewById(R.id.reestructura_exesposo_monto)
        regimenConyugal = findViewById(R.id.reestructura_regimen_conyugal)
        viveConConyuge = findViewById(R.id.reestructura_vive_con_conyuge)
        fechaNoConvive = findViewById(R.id.reestructura_fecha_no_convive)

        guardar = findViewById(R.id.btnGuardar)
        siguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        val camposObligatorios = listOf(
            Pair(motivo, "Motivo"),
            Pair(documento, "Documento"),
            Pair(tipoDocumento, "Tipo de documento"),
            Pair(solicitanteEsAcred, "¿Solicitante es acreditado?"),
            Pair(solicitanteNombre, "Nombre del solicitante"),
            Pair(parentesco, "Parentesco"),
            Pair(motivoPersonal, "Motivo personal"),
            Pair(sexo, "Sexo"),
            Pair(fechaNacimiento, "Fecha de nacimiento"),
            Pair(edad, "Edad"),
            Pair(lugarNacimiento, "Lugar de nacimiento"),
            Pair(gradoEstudios, "Grado de estudios"),
            Pair(conocimientoComp, "Conocimiento computacional"),
            Pair(discapacidad, "Discapacidad"),
            Pair(dictamen, "Dictamen"),
            Pair(institucionDictamen, "Institución del dictamen"),
            Pair(fechaDictamen, "Fecha del dictamen"),
            Pair(porcentajeDiscapacidad, "Porcentaje discapacidad"),
            Pair(estadoCivil, "Estado civil"),
            Pair(fechaEstadoCivil, "Fecha estado civil"),
            Pair(exesposoAportacion, "Exesposo aportación"),
            Pair(exesposoMonto, "Monto del exesposo"),
            Pair(regimenConyugal, "Régimen conyugal"),
            Pair(viveConConyuge, "Vive con cónyuge"),
            Pair(fechaNoConvive, "Fecha no convive")
        )

        // Validate required fields
        for ((campo, nombre) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, completa el campo: $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validate dates
        val fechas = listOf(
            Pair(fechaNacimiento, "Fecha de nacimiento"),
            Pair(fechaDictamen, "Fecha dictamen"),
            Pair(fechaEstadoCivil, "Fecha estado civil"),
            Pair(fechaNoConvive, "Fecha no convive")
        )

        for ((campo, nombre) in fechas) {
            try {
                dateFormat.parse(campo.text.toString())
            } catch (e: Exception) {
                mostrarDialogoValidacion(
                    "Fecha inválida",
                    "Formato inválido en $nombre (usa formato dd/MM/yyyy)",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validate numeric fields
        val camposNumericos = listOf(
            Pair(edad, "Edad"),
            Pair(porcentajeDiscapacidad, "Porcentaje discapacidad"),
            Pair(exesposoMonto, "Monto del exesposo")
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().toDoubleOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El campo '$nombre' debe ser numérico",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

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
        val datos = DatosReestructura(
            reestructura_motivo = motivo.text.toString(),
            reestructura_documento = documento.text.toString(),
            reestructura_tipo_documento = tipoDocumento.text.toString(),
            reestructura_solicitante_es_acred = solicitanteEsAcred.text.toString(),
            reestructura_solicitante_nombre = solicitanteNombre.text.toString(),
            reestructura_parentesco = parentesco.text.toString(),
            reestructura_motivo_personal = motivoPersonal.text.toString(),
            reestructura_sexo = sexo.text.toString(),
            reestructura_fecha_nacimiento = fechaNacimiento.text.toString(),
            reestructura_edad = edad.text.toString(),
            reestructura_lugar_nacimiento = lugarNacimiento.text.toString(),
            reestructura_grado_estudios = gradoEstudios.text.toString(),
            reestructura_conocimiento_comp = conocimientoComp.text.toString(),
            reestructura_discapacidad = discapacidad.text.toString(),
            reestructura_dictamen = dictamen.text.toString(),
            reestructura_institucion_dictamen = institucionDictamen.text.toString(),
            reestructura_fecha_dictamen = fechaDictamen.text.toString(),
            reestructura_porcentaje_discapacidad = porcentajeDiscapacidad.text.toString(),
            reestructura_estado_civil = estadoCivil.text.toString(),
            reestructura_fecha_estado_civil = fechaEstadoCivil.text.toString(),
            reestructura_exesposo_aportacion = exesposoAportacion.text.toString(),
            reestructura_exesposo_monto = exesposoMonto.text.toString(),
            reestructura_regimen_conyugal = regimenConyugal.text.toString(),
            reestructura_vive_con_conyuge = viveConConyuge.text.toString(),
            reestructura_fecha_no_convive = fechaNoConvive.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosReestrucutra(datos)
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

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte6Activity::class.java)
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