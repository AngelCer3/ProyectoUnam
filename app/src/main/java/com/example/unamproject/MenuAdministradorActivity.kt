package com.example.unamproject

import AcreditadosAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.actualizar.actualizarFormatoparte1
import kotlinx.coroutines.launch

class MenuAdministradorActivity : AppCompatActivity() {

    private lateinit var agregar: Button
    private lateinit var rvAcreditados: RecyclerView
    private lateinit var cerrarSesion: Button
    private lateinit var adapter: AcreditadosAdapter
    private var idUsuario: String? = null

    private val retrofitService = RetrofitClient.webService

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_administrador)

        agregar = findViewById(R.id.btn_agregar)
        rvAcreditados = findViewById(R.id.recyclerAcreditados)
        rvAcreditados.layoutManager = LinearLayoutManager(this)

        cerrarSesion = findViewById(R.id.btn_cerrar_sesion)

        agregar.setOnClickListener {
            agregarFormato()
        }

        cerrarSesion.setOnClickListener {
            mostrarDialogo(
                titulo = "Sesión finalizada",
                mensaje = "Has cerrado sesión exitosamente.",
                iconoResId = android.R.drawable.ic_lock_power_off,
                colorTitulo = 0xFF388E3C.toInt()
            ) {
                cerrarSesion()
            }
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
                val listaAcreditados = retrofitService.ObtenerAcreditados()

                if (listaAcreditados.isEmpty()) {
                    mostrarDialogo(
                        titulo = "Sin registros",
                        mensaje = "No hay acreditados para mostrar.",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFFD32F2F.toInt() // rojo
                    )
                } else {
                    adapter = AcreditadosAdapter(listaAcreditados) { acreditado ->
                        val intent = Intent(this@MenuAdministradorActivity, actualizarFormatoparte1::class.java).apply {
                            putExtra("id_usuario", acreditado.id_usuario)
                            putExtra("acreditado", acreditado)
                        }
                        startActivity(intent)
                    }

                    rvAcreditados.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MenuAdministradorActivity, "Error al cargar acreditados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun mostrarDialogo(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val icon = view.findViewById<android.widget.ImageView>(R.id.ivIcon)
        val title = view.findViewById<android.widget.TextView>(R.id.tvTitle)
        val message = view.findViewById<android.widget.TextView>(R.id.tvMessage)
        val btnOk = view.findViewById<android.widget.Button>(R.id.btnOk)

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
