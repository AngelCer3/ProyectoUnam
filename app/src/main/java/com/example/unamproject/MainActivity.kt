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

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnAcceder: Button
    private lateinit var btnAdmin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los componentes del layout
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnAcceder = findViewById(R.id.btn_acceder)
        btnAdmin = findViewById(R.id.btn_admin)

        // Botón "Acceder" como trabajador
        btnAcceder.setOnClickListener {
            val correo = etUsername.text.toString().trim()
            val contrasena = etPassword.text.toString().trim()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                iniciarSesionTrabajador(correo, contrasena)
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para ir al login de administrador
        btnAdmin.setOnClickListener {
            val intent = Intent(this, AdministradorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesionTrabajador(correo: String, contrasena: String) {
        val loginRequest = LoginRequest(correo, contrasena)

        RetrofitClient.webService.iniciarSesion(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!.usuario
                        if (usuario.id_rol == 1) {
                            val intent = Intent(this@MainActivity, MenuTrabajadorActivity::class.java)
                            intent.putExtra("id_usuario", usuario.id_usuario)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Este acceso es solo para trabajadores", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
