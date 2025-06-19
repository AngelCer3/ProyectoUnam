package com.example.unamproject.SinConexion

import AdaptadorAcreditadoSinConexion
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MenuSinConexion : AppCompatActivity(), AdaptadorAcreditadoSinConexion.OnSubirClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAgregar: Button
    private var idUsuario: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_sin_conexion)

        btnAgregar = findViewById(R.id.btn_agregar)
        recyclerView = findViewById(R.id.recyclerAcreditadosTrabajadorSinConexion)
        recyclerView.layoutManager = LinearLayoutManager(this)

        idUsuario = obtenerIdUsuarioDesdeSesion()

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, Formatoparte1ActivitySinConexion::class.java))
        }

        val btnCerrarSesion = findViewById<Button>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí") { dialog, _ ->
                    // Limpiar sesión y resetear idUsuario
                    getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit().clear().apply()
                    idUsuario = -1
                    dialog.dismiss()

                    // Mostrar alerta personalizada de sesión cerrada
                    val alertaSesionCerrada = AlertDialog.Builder(this)
                        .setTitle("Sesión cerrada")
                        .setMessage("Has cerrado sesión correctamente.")
                        .setPositiveButton("OK") { alertDialog, _ ->
                            alertDialog.dismiss()
                            // Redirigir a LoginActivity y cerrar esta activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .create()

                    alertaSesionCerrada.show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        cargarAcreditadosLocales()
    }

    private fun cargarAcreditadosLocales() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val acreditados = db.acreditadoDao().getAllAcreditados()
            recyclerView.adapter = AdaptadorAcreditadoSinConexion(acreditados, this@MenuSinConexion)
        }
    }

    override fun onSubirClick(acreditado: AcreditadoEntity) {
        if (idUsuario == -1) {
            mostrarDialogoLogin(acreditado)
        } else {
            subirDatos(acreditado)
        }
    }

    private fun mostrarDialogoLogin(acreditado: AcreditadoEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        val etCorreo = dialogView.findViewById<EditText>(R.id.etCorreo)
        val etContrasena = dialogView.findViewById<EditText>(R.id.etContrasena)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Login para subir datos")
            .setView(dialogView)
            .setPositiveButton("Iniciar sesión", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnLogin = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnLogin.setOnClickListener {
                val correo = etCorreo.text.toString().trim()
                val contrasena = etContrasena.text.toString()

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    mostrarDialogoCustom("Campos vacíos", "Correo y contraseña son obligatorios", false)
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val exito = hacerLogin(correo, contrasena)
                    if (exito) {
                        dialog.dismiss()
                        subirDatos(acreditado)
                    } else {
                        mostrarDialogoCustom("Credenciales inválidas", "Correo o contraseña incorrectos", false)
                    }
                }
            }
        }

        dialog.show()
    }

    private suspend fun hacerLogin(correo: String, contrasena: String): Boolean = suspendCoroutine { cont ->
        val call = RetrofitClient.webService.iniciarSesion(LoginRequest(correo, contrasena))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.usuario != null) {
                            // Guardar sesión
                            val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE).edit()
                            prefs.putInt("id_usuario", body.usuario.id_usuario)
                            prefs.putString("correo", body.usuario.correo)
                            prefs.apply()

                            idUsuario = body.usuario.id_usuario
                            cont.resume(true)
                        } else {
                            runOnUiThread {
                                mostrarDialogoCustom(
                                    "Error en login",
                                    "Respuesta vacía o formato incorrecto",
                                    false
                                )
                            }
                            cont.resume(false)
                        }
                    }
                    response.code() == 500 -> {
                        runOnUiThread {
                            mostrarDialogoCustom(
                                "Error del servidor",
                                "Intente más tarde (Error 500)",
                                false
                            )
                        }
                        cont.resume(false)
                    }
                    else -> {
                        runOnUiThread {
                            mostrarDialogoCustom(
                                "Error en login",
                                "Credenciales incorrectas o problema con el servidor (Código ${response.code()})",
                                false
                            )
                        }
                        cont.resume(false)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                runOnUiThread {
                    mostrarDialogoCustom(
                        "Error de conexión",
                        t.localizedMessage ?: "Error desconocido al conectar con el servidor",
                        false
                    )
                }
                cont.resume(false)
            }
        })
    }

    private fun subirDatos(acreditado: AcreditadoEntity) {
        if (idUsuario == -1) {
            mostrarDialogoCustom("Error", "Usuario no autenticado", false)
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            val datosCompletos = DatosCompletosRequest(
                id_usuario = idUsuario.toString(),
                tGenerales = acreditado,
                tVisitas = db.visitasDao().getVisitasByAcreditado(acreditado.id_acreditado.toString()),
                tVivienda = db.datosViviendaDao().getDatosViviendaByAcreditado(acreditado.id_acreditado.toString()),
                tCredito = db.datosCreditoDao().getDatosCreditoByAcreditado(acreditado.id_acreditado.toString()),
                tReestructura = db.datosReestructuraDao().getDatosReestructuraByAcreditado(acreditado.id_acreditado.toString()),
                tConyuge = db.datosConyugeDao().getDatosConyugeByAcreditado(acreditado.id_acreditado.toString()),
                tFamiliares = db.datosFamiliaresDao().getDatosFamiliaresByAcreditado(acreditado.id_acreditado.toString()),
                tSolicitante = db.datosSolicitanteDao().getDatosSolicitanteByAcreditado(acreditado.id_acreditado.toString()),
                tEspeciConyuge = db.datosEspecificosConyugeDao().getDatosEspecificosConyugeByAcreditado(acreditado.id_acreditado.toString()),
                tOtrosFamiliares = db.datosOtrosFamiliaresDao().getDatosOtrosFamiliaresByAcreditado(acreditado.id_acreditado.toString()),
                tGastos = db.datosGastosDao().getDatosGastosByAcreditado(acreditado.id_acreditado.toString()),
                tDeudas = db.datosFamiliaDeudasDao().getDatosFamiliaDeudasByAcreditado(acreditado.id_acreditado.toString()),
                tTelefonos = db.datosTelefonosDao().getDatosTelefonosByAcreditado(acreditado.id_acreditado.toString()),
                tCobranza = db.datosCobranzaDao().getDatosCobranzaByAcreditado(acreditado.id_acreditado.toString()),
                tDocumentos = db.datosDocumentosDao().getDatosDocumentosByAcreditado(acreditado.id_acreditado.toString()),
                tEspeciVivienda = db.datosEspecificosViviendaDao().getDatosEspecificosViviendaByAcreditado(acreditado.id_acreditado.toString()),
                tObservaciones = db.datosObservacionesDao().getDatosObservacionesByAcreditado(acreditado.id_acreditado.toString()),
                tCoordenadas = db.datosCoordenadasDao().getDatosCoordenadasByAcreditado(acreditado.id_acreditado.toString())
            )

            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.webService.enviarDatosCompletos(datosCompletos)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            mostrarDialogoCustom("Éxito", "Datos subidos correctamente", true)
                        } else {
                            mostrarDialogoCustom("Error al subir", "Código: ${response.code()}", false)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mostrarDialogoCustom("Error de red", e.localizedMessage ?: "Error desconocido", false)
                    }
                }
            }
        }
    }

    private fun obtenerIdUsuarioDesdeSesion(): Int {
        val prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE)
        return prefs.getInt("id_usuario", -1)
    }

    private fun mostrarDialogoCustom(titulo: String, mensaje: String, exito: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        val icon = dialogView.findViewById<android.widget.ImageView>(R.id.ivIcon)
        val tituloView = dialogView.findViewById<android.widget.TextView>(R.id.tvTitle)
        val mensajeView = dialogView.findViewById<android.widget.TextView>(R.id.tvMessage)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        icon.setImageResource(if (exito) R.drawable.ic_success else R.drawable.ic_delete)
        tituloView.text = titulo
        mensajeView.text = mensaje

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnOk.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
