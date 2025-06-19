package com.example.unamproject

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.SinConexion.MenuSinConexion
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

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnAcceder = findViewById(R.id.btn_acceder)
        btnAdmin = findViewById(R.id.btn_admin)

        btnAcceder.setOnClickListener {
            if (!hayConexionInternet(this)) {
                mostrarAlertaPersonalizada(
                    titulo = "Sin conexión",
                    mensaje = "No hay conexión a Internet. Redirigiendo a modo sin conexión...",
                    icono = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt() // rojo
                )
                val intent = Intent(this, MenuSinConexion::class.java)
                startActivity(intent)
                return@setOnClickListener
            }

            val correo = etUsername.text.toString().trim()
            val contrasena = etPassword.text.toString().trim()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                iniciarSesionTrabajador(correo, contrasena)
            } else {
                mostrarAlertaPersonalizada(
                    titulo = "Campos vacíos",
                    mensaje = "Por favor, ingrese usuario y contraseña.",
                    icono = android.R.drawable.ic_dialog_info,
                    colorTitulo = 0xFF1976D2.toInt() // azul
                )
            }
        }

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
                        val usuarioResponse = response.body()!!

                        if (usuario.id_rol == 2) {
                            mostrarAlertaPersonalizada(
                                titulo = "Bienvenido",
                                mensaje = "Acceso exitoso",
                                icono = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF388E3C.toInt() // verde
                            )
                            val intent = Intent(this@MainActivity, MenuTrabajadorActivity::class.java)
                            intent.putExtra("id_usuario", usuarioResponse.usuario.id_usuario.toString())
                            startActivity(intent)
                        } else {
                            mostrarAlertaPersonalizada(
                                titulo = "Acceso denegado",
                                mensaje = "Este acceso es solo para trabajadores.",
                                icono = android.R.drawable.ic_delete,
                                colorTitulo = 0xFFD32F2F.toInt() // rojo
                            )
                        }
                    } else {
                        mostrarAlertaPersonalizada(
                            titulo = "Credenciales inválidas",
                            mensaje = "Usuario o contraseña incorrectos.",
                            icono = android.R.drawable.ic_dialog_alert,
                            colorTitulo = 0xFFD32F2F.toInt() // rojo
                        )
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    mostrarAlertaPersonalizada(
                        titulo = "Error de red",
                        mensaje = "No se pudo conectar: ${t.message}",
                        icono = android.R.drawable.stat_notify_error,
                        colorTitulo = 0xFFD32F2F.toInt() // rojo
                    )
                }
            })
    }

    private fun hayConexionInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }

    // Función personalizada para mostrar AlertDialog con icono y título coloreado y tamaño ajustado
    private fun mostrarAlertaPersonalizada(titulo: String, mensaje: String, icono: Int, colorTitulo: Int) {
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val ivIcon = dialogView.findViewById<ImageView>(R.id.ivIcon)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        ivIcon.setImageResource(icono)
        ivIcon.setColorFilter(colorTitulo)  // Color dinámico para el icono

        tvTitle.text = titulo
        tvTitle.setTextColor(colorTitulo)
        tvMessage.text = mensaje

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
