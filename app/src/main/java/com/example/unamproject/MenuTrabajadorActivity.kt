package com.example.unamproject

import AcreditadosTrabajadorAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MenuTrabajadorActivity : AppCompatActivity() {

    private lateinit var agregar: Button
    private lateinit var cerrarSesion: Button
    private lateinit var recyclerAcreditadosTrabajador: RecyclerView
    private lateinit var adapter: AcreditadosTrabajadorAdapter
    private var idUsuario: String? = null

    private val retrofitService = RetrofitClient.webService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_trabajador)

        idUsuario = intent.getStringExtra("id_usuario")

        agregar = findViewById(R.id.btn_agregar)
        cerrarSesion = findViewById(R.id.btn_cerrar_sesion)
        recyclerAcreditadosTrabajador = findViewById(R.id.recyclerAcreditadosTrabajador)
        recyclerAcreditadosTrabajador.layoutManager = LinearLayoutManager(this)

        cerrarSesion.setOnClickListener {
            mostrarDialogoPersonalizado(
                titulo = "Sesión cerrada",
                mensaje = "Has cerrado sesión correctamente.",
                iconoResId = android.R.drawable.ic_lock_power_off,
                colorTitulo = 0xFF388E3C.toInt()
            ) {
                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit().clear().apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        agregar.setOnClickListener {
            agregarFormato()
        }

        if (idUsuario.isNullOrEmpty()) {
            mostrarDialogoPersonalizado(
                titulo = "Error",
                mensaje = "Usuario no identificado.",
                iconoResId = android.R.drawable.stat_notify_error,
                colorTitulo = 0xFFD32F2F.toInt() // rojo
            ) {
                finish()
            }
            return
        }

        cargarAcreditados()
    }

    private fun agregarFormato() {
        val intent = Intent(this, Formatoparte1Activity::class.java)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }

    private fun cargarAcreditados() {
        lifecycleScope.launch {
            try {
                val listaAcreditados = retrofitService.ObtenerAcreditadoPorId(idUsuario!!)

                if (listaAcreditados.isEmpty()) {
                    mostrarDialogoPersonalizado(
                        titulo = "Sin registros",
                        mensaje = "No hay acreditados disponibles para mostrar.",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF1976D2.toInt() // azul
                    )
                } else {
                    adapter = AcreditadosTrabajadorAdapter(listaAcreditados)
                    recyclerAcreditadosTrabajador.adapter = adapter
                }

            } catch (e: Exception) {
                e.printStackTrace()
                mostrarDialogoPersonalizado(
                    titulo = "Error",
                    mensaje = "Error al cargar acreditados.",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt() // rojo
                )
            }
        }
    }

    // -------------------------
    // Función de alerta común
    // -------------------------
    private fun mostrarDialogoPersonalizado(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val imageViewIcon = dialogView.findViewById<ImageView>(R.id.ivIcon)
        val textViewTitulo = dialogView.findViewById<TextView>(R.id.tvTitle)
        val textViewMensaje = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        imageViewIcon.setImageResource(iconoResId)
        imageViewIcon.setColorFilter(colorTitulo)
        textViewTitulo.text = titulo
        textViewTitulo.setTextColor(colorTitulo)
        textViewMensaje.text = mensaje

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
            onAceptar?.invoke()
        }

        dialog.show()
    }
}
