package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
                mostrarDialogo(
                    titulo = "Campos vacíos",
                    mensaje = "Ingrese usuario y contraseña.",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFF1976D2.toInt()
                )
            }
        }

        regresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun iniciarSesionAdministrador(correo: String, contrasena: String) {
        val loginRequest = LoginRequest(correo, contrasena)

        RetrofitClient.webService.iniciarSesion(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val usuario = response.body()!!.usuario
                        val usuarioResponse = response.body()!!
                        if (usuario.id_rol == 1) {
                            mostrarDialogo(
                                titulo = "Bienvenido",
                                mensaje = "Acceso exitoso",
                                iconoResId = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF388E3C.toInt()
                            ) {
                                val intent = Intent(this@AdministradorActivity, MenuAdministradorActivity::class.java)
                                intent.putExtra("id_usuario", usuarioResponse.usuario.id_usuario.toString())
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            mostrarDialogo(
                                titulo = "Acceso denegado",
                                mensaje = "Este acceso es solo para administradores.",
                                iconoResId = android.R.drawable.stat_notify_error,
                                colorTitulo = 0xFFD32F2F.toInt() // rojo
                            )
                        }
                    } else {
                        mostrarDialogo(
                            titulo = "Credenciales inválidas",
                            mensaje = "Verifica tu usuario y contraseña.",
                            iconoResId = android.R.drawable.ic_lock_idle_lock,
                            colorTitulo = 0xFFD32F2F.toInt() // rojo
                        )
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    mostrarDialogo(
                        titulo = "Error de red",
                        mensaje = "No se pudo conectar: ${t.message}",
                        iconoResId = android.R.drawable.stat_notify_error,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                }
            })
    }

    private fun mostrarDialogo(
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
