package com.example.unamproject

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegistrarTrabajadorActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerRol: Spinner
    private lateinit var btnRegistrar: Button

    private val retrofitService = RetrofitClient.webService

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_trabajador)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        spinnerRol = findViewById(R.id.spinnerRol)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        cargarRoles()

        btnRegistrar.setOnClickListener {
            registrarTrabajador()
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

    private fun registrarTrabajador() {

        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (correo.isEmpty()) {
            etCorreo.error = "Ingrese el correo"
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Ingrese la contraseña"
            return
        }

        val rol = if (spinnerRol.selectedItemPosition == 0) {
            1
        } else {
            2
        }

        lifecycleScope.launch {

            try {

                val response = retrofitService.registrarUsuario(
                    UsuarioRequest(
                        correo,
                        password,
                        rol
                    )
                )

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@RegistrarTrabajadorActivity,
                        "Trabajador registrado correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@RegistrarTrabajadorActivity,
                        "Error al registrar trabajador",
                        Toast.LENGTH_LONG
                    ).show()

                }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@RegistrarTrabajadorActivity,
                    "Error de conexión con el servidor",
                    Toast.LENGTH_LONG
                ).show()

            }

        }
    }
}