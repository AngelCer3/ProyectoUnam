package com.example.unamproject

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditarTrabajadorActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerRol: Spinner
    private lateinit var btnActualizar: Button

    private var idUsuario: Int = 0

    private val retrofitService = RetrofitClient.webService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_trabajador)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        spinnerRol = findViewById(R.id.spinnerRol)
        btnActualizar = findViewById(R.id.btnActualizar)

        idUsuario = intent.getIntExtra("id_usuario", 0)

        cargarRoles()
        obtenerUsuario()

        btnActualizar.setOnClickListener {
            actualizarTrabajador()
        }
    }

    private fun cargarRoles() {

        val roles = listOf(
            "Administrador",
            "Trabajador"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        spinnerRol.adapter = adapter
    }

    private fun obtenerUsuario() {

        lifecycleScope.launch {

            try {

                val response = retrofitService.ObtenerTrabajadorPorId(idUsuario)

                if (response.isSuccessful) {

                    val usuario = response.body()

                    usuario?.let {

                        etCorreo.setText(it.correo)

                        if (it.id_rol == 1) {
                            spinnerRol.setSelection(0)
                        } else {
                            spinnerRol.setSelection(1)
                        }

                    }

                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@EditarTrabajadorActivity,
                    "Error al cargar trabajador",
                    Toast.LENGTH_LONG
                ).show()

            }

        }
    }

    private fun actualizarTrabajador() {

        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val rol = if (spinnerRol.selectedItemPosition == 0) {
            1
        } else {
            2
        }

        lifecycleScope.launch {

            try {

                val request = UsuarioRequest(
                    correo,
                    password,
                    rol
                )

                retrofitService.actualizarRegistro(idUsuario, request)

                Toast.makeText(
                    this@EditarTrabajadorActivity,
                    "Trabajador actualizado correctamente",
                    Toast.LENGTH_LONG
                ).show()

                finish()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@EditarTrabajadorActivity,
                    "Error al actualizar trabajador",
                    Toast.LENGTH_LONG
                ).show()

            }

        }
    }
}