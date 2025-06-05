package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdministradorActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var accederButton: Button
    private lateinit var regresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        accederButton = findViewById(R.id.btn_acceder)
        regresar = findViewById(R.id.btn_regresar)

        accederButton.setOnClickListener {
            val correo = etUsername.text.toString().trim()
            val contrasena = etPassword.text.toString().trim()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                iniciarSesionAdministrador(correo, contrasena)
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        regresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesionAdministrador(correo: String, contrasena: String) {
        val loginRequest = LoginRequest(correo, contrasena)

        RetrofitClient.webService.iniciarSesion(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!.usuario
                        if (usuario.id_rol == 2) { // Validar que sea administrador
                            val intent = Intent(this@AdministradorActivity, MenuAdministradorActivity::class.java)
                            intent.putExtra("id_usuario", usuario.id_usuario)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@AdministradorActivity, "Este acceso es solo para administradores", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AdministradorActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@AdministradorActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
