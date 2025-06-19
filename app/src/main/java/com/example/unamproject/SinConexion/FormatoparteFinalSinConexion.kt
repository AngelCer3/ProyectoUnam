package com.example.unamproject.SinConexion

import android.Manifest
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
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class FormatoparteFinalSinConexion : AppCompatActivity() {

    private var idAcreditado: Long = 0L
    private lateinit var map: MapView
    private lateinit var latitudEditText: EditText
    private lateinit var longitudEditText: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnFinalizar: Button

    private lateinit var datosCoordenadasDao: DatosCoordenadasDao
    private var datosGuardados = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var ubicacionObtenida = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_formatoparte_final_sin_conexion)

        // Inicializar cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        initViews()
        setupMap()
        setupDatabase()
        setupButtons()

        // Intentar obtener ubicación automáticamente al iniciar
        obtenerUbicacionAutomatica()
    }

    private fun initViews() {
        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnFinalizar = findViewById(R.id.btnSiguiente)
    }

    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332) // CDMX
        map.controller.setZoom(14.0)
        map.controller.setCenter(startPoint)

        map.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val geoPoint = map.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                actualizarCoordenadas(geoPoint.latitude, geoPoint.longitude, "Ubicación seleccionada")
            }
            false
        }
    }

    private fun setupDatabase() {
        val db = AppDatabase.getDatabase(applicationContext)
        datosCoordenadasDao = db.datosCoordenadasDao()
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCoordenadas()) {
                guardarCoordenadas()
            }
        }

        btnFinalizar.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar las coordenadas antes de finalizar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarCoordenadas() }
                )
            } else {
                irSiguiente()
            }
        }
    }

    private fun obtenerUbicacionAutomatica() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            obtenerUbicacionConPermiso()
        }
    }

    private fun obtenerUbicacionConPermiso() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                if (!ubicacionObtenida) { // Solo actualizar si no tenemos ubicación aún
                    ubicacionObtenida = true
                    actualizarCoordenadas(it.latitude, it.longitude, "Tu ubicación actual")
                }
            } ?: run {
                mostrarDialogo(
                    titulo = "Información",
                    mensaje = "No se pudo obtener la ubicación automáticamente. Selecciona una ubicación manualmente en el mapa.",
                    iconoResId = android.R.drawable.ic_dialog_info,
                    colorTitulo = 0xFF2196F3.toInt()
                )
            }
        }
    }

    private fun actualizarCoordenadas(lat: Double, lon: Double, markerTitle: String) {
        latitudEditText.setText(lat.toString())
        longitudEditText.setText(lon.toString())

        val geoPoint = GeoPoint(lat, lon)
        map.controller.setCenter(geoPoint)

        map.overlays.clear()
        val marker = Marker(map)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = markerTitle
        map.overlays.add(marker)
        map.invalidate()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacionConPermiso()
                } else {
                    mostrarDialogo(
                        titulo = "Información",
                        mensaje = "Sin permiso de ubicación. Selecciona una ubicación manualmente en el mapa.",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF2196F3.toInt()
                    )
                }
            }
        }
    }

    private fun validarCoordenadas(): Boolean {
        val lat = latitudEditText.text.toString().trim()
        val lon = longitudEditText.text.toString().trim()

        if (lat.isEmpty() || lon.isEmpty()) {
            mostrarDialogo(
                titulo = "Ubicación requerida",
                mensaje = "Debes seleccionar una ubicación en el mapa",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        try {
            val latValue = lat.toDouble()
            val lonValue = lon.toDouble()

            // Validar rangos aproximados para México
            if (latValue < 14.5 || latValue > 32.7 || lonValue < -118.4 || lonValue > -86.7) {
                mostrarDialogo(
                    titulo = "Ubicación inválida",
                    mensaje = "Las coordenadas no parecen estar dentro de México",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        } catch (e: NumberFormatException) {
            mostrarDialogo(
                titulo = "Formato inválido",
                mensaje = "Las coordenadas deben ser valores numéricos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun guardarCoordenadas() {
        val lat = latitudEditText.text.toString().trim()
        val lon = longitudEditText.text.toString().trim()

        val datos = DatosCoordenadasEntity(
            coordenadaX = lon,
            coordenadaY = lat,
            id_acreditado = idAcreditado.toString()
        )

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    datosCoordenadasDao.insertDatosCoordenadas(datos)
                }

                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Coordenadas guardadas correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar coordenadas: ${e.message ?: "Error desconocido"}")
                }
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

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorYCerrar(mensaje: String) {
        mostrarDialogo(
            titulo = "Error crítico",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        ) {
            finish()
        }
    }

    private fun mostrarDialogo(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)

        view.findViewById<ImageView>(R.id.ivIcon).apply {
            setImageResource(iconoResId)
            setColorFilter(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvTitle).apply {
            text = titulo
            setTextColor(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvMessage).text = mensaje

        AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
            .apply {
                view.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    dismiss()
                    onAceptar?.invoke()
                }
                show()
            }
    }
}