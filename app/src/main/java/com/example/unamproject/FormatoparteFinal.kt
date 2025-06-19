package com.example.unamproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnFinal = findViewById(R.id.btnSiguiente)

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332)
        map.controller.setZoom(14.0)
        map.controller.setCenter(startPoint)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            obtenerUbicacionActual()
        }

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

        btnGuardar.setOnClickListener {
            val lat = latitudEditText.text.toString()
            val lon = longitudEditText.text.toString()

            if (lat.isNotEmpty() && lon.isNotEmpty()) {
                val datos = datosCoordenadas(
                    coordenadaX = lon,
                    coordenadaY = lat,
                    id_acreditado = idAcreditado ?: "",
                    id_usuario = idUsuario ?: ""
                )

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitClient.webService.agregarDatosCoordenadas(datos)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                mostrarAlertaPersonalizada("Coordenadas guardadas correctamente", true)
                            } else {
                                mostrarAlertaPersonalizada("Error al guardar: ${response.code()}", false)
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            mostrarAlertaPersonalizada("Error de red: ${e.localizedMessage}", false)
                        }
                    }
                }
            } else {
                mostrarAlertaPersonalizada("Selecciona un punto en el mapa primero", false)
            }
        }

        btnFinal.setOnClickListener {
            if (idUsuario == null) {
                mostrarAlertaPersonalizada("ID de usuario no disponible", false)
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.webService.obtenerUsuarioPorId(idUsuario!!.toInt())
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val usuario = response.body()
                            when (usuario?.id_rol) {
                                1 -> startActivity(Intent(this@FormatoparteFinal, MenuAdministradorActivity::class.java).putExtra("id_usuario", idUsuario))
                                2 -> startActivity(Intent(this@FormatoparteFinal, MenuTrabajadorActivity::class.java).putExtra("id_usuario", idUsuario))
                                else -> mostrarAlertaPersonalizada("Rol de usuario no reconocido", false)
                            }
                            finish()
                        } else {
                            mostrarAlertaPersonalizada("Error al obtener el rol del usuario", false)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mostrarAlertaPersonalizada("Error de red: ${e.localizedMessage}", false)
                    }
                }
            }
        }
    }

    private fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude

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
                } ?: run {
                    mostrarAlertaPersonalizada("No se pudo obtener la ubicación", false)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacionActual()
        } else {
            mostrarAlertaPersonalizada("Se necesita permiso de ubicación para mostrar tu posición", false)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun mostrarAlertaPersonalizada(mensaje: String, esExito: Boolean) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        val icon = dialogView.findViewById<ImageView>(R.id.ivIcon)
        val title = dialogView.findViewById<TextView>(R.id.tvTitle)
        val message = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        icon.setImageResource(if (esExito) R.drawable.ic_success else R.drawable.ic_delete)
        title.text = if (esExito) "Éxito" else "Error"
        message.text = mensaje

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
