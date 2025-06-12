package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.identificarAcreditado
import kotlinx.coroutines.launch

class actualizarFormatoparte1 : AppCompatActivity() {

    // Declara todos tus EditText como lateinit
    private lateinit var entidadFederativa: EditText
    private lateinit var ciudadMunicipioDelegacion: EditText
    private lateinit var apellidoPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var nombres: EditText
    private lateinit var calle: EditText
    private lateinit var condominio: EditText
    private lateinit var it: EditText
    private lateinit var mz: EditText
    private lateinit var noExt: EditText
    private lateinit var noInt: EditText
    private lateinit var edif: EditText
    private lateinit var colonia: EditText
    private lateinit var cp: EditText
    private lateinit var curp: EditText
    private var idUsuario: String? = null


    private lateinit var acreditado: identificarAcreditado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte1)

        idUsuario = intent.getStringExtra("id_usuario")

        // Inicializa todas las vistas
        initViews()

        // Obtiene el objeto del intent
        acreditado = intent.getSerializableExtra("acreditado") as? identificarAcreditado ?: run {
            Toast.makeText(this, "Error: Datos no recibidos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Carga los datos en los campos
        cargarDatos()

        // Configura los botones
        findViewById<Button>(R.id.btnActualizarDatos).setOnClickListener {
            if (validarCampos()) {
                actualizarDatos()
            }
        }

        findViewById<Button>(R.id.btnSiguiente).setOnClickListener {
            navegarAParte2()
        }
    }

    private fun initViews() {
        entidadFederativa = findViewById(R.id.entidad_federativa)
        ciudadMunicipioDelegacion = findViewById(R.id.ciudad_municipio_delegacion)
        apellidoPaterno = findViewById(R.id.apellido_paterno)
        apellidoMaterno = findViewById(R.id.apellido_materno)
        nombres = findViewById(R.id.nombres)
        calle = findViewById(R.id.domicilio_calle)
        condominio = findViewById(R.id.domicilio_condominio)
        it = findViewById(R.id.domicilio_it)
        mz = findViewById(R.id.domicilio_mz)
        noExt = findViewById(R.id.domicilio_no_ext)
        noInt = findViewById(R.id.domicilio_no_int)
        edif = findViewById(R.id.domicilio_edif)
        colonia = findViewById(R.id.domicilio_colonia)
        cp = findViewById(R.id.domicilio_cp)
        curp = findViewById(R.id.domicilio_curp)
    }

    private fun cargarDatos() {
        entidadFederativa.setText(acreditado.entidad_federativa)
        ciudadMunicipioDelegacion.setText(acreditado.ciudad_municipio_delegacion)
        apellidoPaterno.setText(acreditado.apellido_paterno)
        apellidoMaterno.setText(acreditado.apellido_materno)
        nombres.setText(acreditado.nombres)
        calle.setText(acreditado.domicilio_calle)
        condominio.setText(acreditado.domicilio_condominio)
        it.setText(acreditado.domicilio_it)
        mz.setText(acreditado.domicilio_mz)
        noExt.setText(acreditado.domicilio_no_ext)
        noInt.setText(acreditado.domicilio_no_int)
        edif.setText(acreditado.domicilio_edif)
        colonia.setText(acreditado.domicilio_colonia)
        cp.setText(acreditado.domicilio_cp)
        curp.setText(acreditado.domicilio_curp)
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (apellidoPaterno.text.isNullOrBlank()) {
            apellidoPaterno.error = "Requerido"
            valido = false
        }

        // Agrega validaciones para otros campos importantes

        return valido
    }

    private fun actualizarDatos() {
        val acreditadoActualizado = identificarAcreditado(
            id_acreditado = acreditado.id_acreditado,
            entidad_federativa = entidadFederativa.text.toString(),
            ciudad_municipio_delegacion = ciudadMunicipioDelegacion.text.toString(),
            apellido_paterno = apellidoPaterno.text.toString(),
            apellido_materno = apellidoMaterno.text.toString(),
            nombres = nombres.text.toString(),
            domicilio_calle = calle.text.toString(),
            domicilio_condominio = condominio.text.toString(),
            domicilio_it = it.text.toString(),
            domicilio_mz = mz.text.toString(),
            domicilio_no_ext = noExt.text.toString(),
            domicilio_no_int = noInt.text.toString(),
            domicilio_edif = edif.text.toString(),
            domicilio_colonia = colonia.text.toString(),
            domicilio_cp = cp.text.toString(),
            domicilio_curp = curp.text.toString(),
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarAcreditado(
                    acreditado.id_acreditado.toString(),
                    acreditadoActualizado
                )

                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte1, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                    acreditado = acreditadoActualizado
                } else {
                    Toast.makeText(this@actualizarFormatoparte1, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte1, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navegarAParte2() {
        val intent = Intent(this, actualizarFormatoparte2::class.java).apply {
            putExtra("id_acreditado", acreditado.id_acreditado) // o acreditado.id según tu data class
            intent.putExtra("id_usuario", idUsuario)
        }
        startActivity(intent)
    }

}