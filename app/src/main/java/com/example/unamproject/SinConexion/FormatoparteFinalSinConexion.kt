package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.MenuTrabajadorActivity
import com.example.unamproject.R
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class FormatoparteFinalSinConexion : AppCompatActivity() {

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    private lateinit var map: MapView
    private lateinit var latitudEditText: EditText
    private lateinit var longitudEditText: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnFinalizar: Button

    private lateinit var datosCoordenadasDao: DatosCoordenadasDao  // DAO para Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_formatoparte_final_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Inicializa vistas
        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnFinalizar = findViewById(R.id.btnSiguiente)

        // Configura mapa
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332) // CDMX
        map.controller.setZoom(14.0)
        map.controller.setCenter(startPoint)

        // Inicializa DAO (ajusta esto a cómo instancias tu base Room)
        val db = AppDatabase.getDatabase(applicationContext)
        datosCoordenadasDao = db.datosCoordenadasDao()

        // Detecta toque para seleccionar ubicación
        map.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val geoPoint = map.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                val lat = geoPoint.latitude
                val lon = geoPoint.longitude

                latitudEditText.setText(lat.toString())
                longitudEditText.setText(lon.toString())

                map.overlays.clear()
                val marker = Marker(map)
                marker.position = geoPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Ubicación seleccionada"
                map.overlays.add(marker)
                map.invalidate()
            }
            false
        }

        btnFinalizar.setOnClickListener {
            irSiguiente()
        }

        // Guardar localmente con Room
        btnGuardar.setOnClickListener {
            val lat = latitudEditText.text.toString()
            val lon = longitudEditText.text.toString()

            if (lat.isNotEmpty() && lon.isNotEmpty()) {
                val datos = DatosCoordenadasEntity(
                    coordenadaX = lon,
                    coordenadaY = lat,
                    id_acreditado = idAcreditado ?: "",
                )

                lifecycleScope.launch {
                    try {
                        datosCoordenadasDao.insertDatosCoordenadas(datos)
                        runOnUiThread {
                            Toast.makeText(this@FormatoparteFinalSinConexion, "Coordenadas guardadas localmente", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@FormatoparteFinalSinConexion, "Error guardando localmente: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Selecciona un punto en el mapa primero", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun irSiguiente() {
        val intent = Intent(this, MenuSinConexion::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
