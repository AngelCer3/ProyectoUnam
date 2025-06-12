package com.example.unamproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class FormatoparteFinal : AppCompatActivity() {

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    private lateinit var map: MapView
    private lateinit var latitudEditText: EditText
    private lateinit var longitudEditText: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnFinal: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_formatoparte_final)

        // Obtener IDs del Intent y sesión
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Inicializar vistas
        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnFinal = findViewById(R.id.btnSiguiente)

        // Configurar mapa
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332) // CDMX (punto inicial por defecto)
        map.controller.setZoom(14.0)
        map.controller.setCenter(startPoint)

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verificar y pedir permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            obtenerUbicacionActual()
        }

        // Marcar ubicación al tocar el mapa manualmente
        map.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val projection = map.projection
                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
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

        // Guardar coordenadas
        btnGuardar.setOnClickListener {
            val lat = latitudEditText.text.toString()
            val lon = longitudEditText.text.toString()

            if (lat.isNotEmpty() && lon.isNotEmpty()) {
                val datos = datosCoordenadas(
                    coordenadaX = lon,            // Longitud
                    coordenadaY = lat,            // Latitud
                    id_acreditado = idAcreditado ?: "",
                    id_usuario = idUsuario ?: ""
                )

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitClient.webService.agregarDatosCoordenadas(datos)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                Toast.makeText(this@FormatoparteFinal, "Coordenadas guardadas correctamente", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@FormatoparteFinal, "Error al guardar: ${response.code()}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@FormatoparteFinal, "Error de red: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Selecciona un punto en el mapa primero", Toast.LENGTH_SHORT).show()
            }
        }

        btnFinal.setOnClickListener { irSiguiente() }

    }

    private fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    latitudEditText.setText(lat.toString())
                    longitudEditText.setText(lon.toString())

                    val geoPoint = GeoPoint(lat, lon)
                    map.controller.setCenter(geoPoint)

                    map.overlays.clear()
                    val marker = Marker(map)
                    marker.position = geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Tu ubicación actual"
                    map.overlays.add(marker)
                    map.invalidate()
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual()
            } else {
                Toast.makeText(this, "Se necesita permiso de ubicación para mostrar tu posición", Toast.LENGTH_SHORT).show()
            }
        }
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
