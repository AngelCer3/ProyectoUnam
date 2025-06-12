package com.example.unamproject

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        // Referencias a los componentes del layout
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnAcceder = findViewById(R.id.btn_acceder)
        btnAdmin = findViewById(R.id.btn_admin)

        // Botón "Acceder" como trabajador
        btnAcceder.setOnClickListener {
            // Verificar conexión a Internet
            if (!hayConexionInternet(this)) {
                Toast.makeText(this, "Sin conexión a Internet. Redirigiendo a modo sin conexión...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuSinConexion::class.java)
                startActivity(intent)
                return@setOnClickListener
            }

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
                        val usuarioResponse = response.body()!!
                        if (usuario.id_rol == 1) {
                            val intent = Intent(this@MainActivity, MenuTrabajadorActivity::class.java)
                            intent.putExtra("id_usuario", usuarioResponse.usuario.id_usuario.toString())
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
}
