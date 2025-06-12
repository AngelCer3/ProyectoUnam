package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.MenuTrabajadorActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosCoordenadas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class actualizarFormatoparteFinal : AppCompatActivity() {

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    private lateinit var map: MapView
    private lateinit var latitudEditText: EditText
    private lateinit var longitudEditText: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnFinal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(R.layout.activity_actualizar_formatoparte_final)

        // Obtener IDs
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Vistas
        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnFinal = findViewById(R.id.btnSiguiente)

        // Configurar mapa
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332)
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        // Manejar toques en el mapa
        val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    latitudEditText.setText(it.latitude.toString())
                    longitudEditText.setText(it.longitude.toString())

                    map.overlays.removeAll { it is Marker }

                    val marker = Marker(map)
                    marker.position = it
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Ubicación seleccionada"
                    map.overlays.add(marker)
                    map.invalidate()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        })
        map.overlays.add(eventsOverlay)

        // Botón actualizar
        btnActualizar.setOnClickListener {
            val lat = latitudEditText.text.toString()
            val lon = longitudEditText.text.toString()

            if (lat.isNotEmpty() && lon.isNotEmpty()) {
                actualizarCoordenadas(idAcreditado!!, lat, lon)
            } else {
                Toast.makeText(this, "Selecciona un punto en el mapa", Toast.LENGTH_SHORT).show()
            }
        }

        // Siguiente
        btnFinal.setOnClickListener { irSiguiente() }

        // Obtener coordenadas actuales
        obtenerCoordenadas()
    }

    private fun actualizarCoordenadas(id: String, lat: String, lon: String) {
        val datos = datosCoordenadas(
            coordenadaX = lon,  // Longitud
            coordenadaY = lat,  // Latitud
            id_acreditado = id, // Ya que también va en el body
            id_usuario = idUsuario ?: ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosCoordenadas(id, datos)
                runOnUiThread {
                    if (response.isSuccessful && response.body()?.message == "Coordenadas actualizadas correctamente") {
                        Toast.makeText(this@actualizarFormatoparteFinal, "Coordenadas actualizadas con éxito", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@actualizarFormatoparteFinal, "Error al actualizar: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@actualizarFormatoparteFinal, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun obtenerCoordenadas() {
        // Supón que esto es una llamada a API o base local
        val latitud = 19.4326
        val longitud = -99.1332

        latitudEditText.setText(latitud.toString())
        longitudEditText.setText(longitud.toString())

        val point = GeoPoint(latitud, longitud)
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Ubicación actual"
        map.overlays.add(marker)
        map.controller.setCenter(point)
        map.invalidate()
    }

    private fun irSiguiente() {
        val intent = Intent(this, MenuTrabajadorActivity::class.java)
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
