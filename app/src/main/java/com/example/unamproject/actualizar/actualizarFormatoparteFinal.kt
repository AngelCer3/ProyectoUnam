package com.example.unamproject.actualizar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosCoordenadas
import com.example.unamproject.MenuAdministradorActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparteFinal : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var latitudEditText: EditText
    private lateinit var longitudEditText: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnFinal: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_actualizar_formatoparte_final)

        initViews()
        setupMap()
        setupButtons()

        // Validar idAcreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

        cargarDatosCoordenadas()
    }

    private fun initViews() {
        map = findViewById(R.id.map)
        latitudEditText = findViewById(R.id.latitudEditText)
        longitudEditText = findViewById(R.id.longitudEditText)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnFinal = findViewById(R.id.btnSiguiente)
    }

    private fun setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val startPoint = GeoPoint(19.4326, -99.1332) // Centro de la CDMX
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    latitudEditText.setText(it.latitude.toString())
                    longitudEditText.setText(it.longitude.toString())

                    map.overlays.removeAll { overlay -> overlay is Marker }
                    val marker = Marker(map).apply {
                        position = it
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Ubicación seleccionada"
                    }
                    map.overlays.add(marker)
                    map.invalidate()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        })
        map.overlays.add(eventsOverlay)
    }

    private fun setupButtons() {
        btnActualizar.setOnClickListener {
            if (validarCoordenadas()) {
                mostrarConfirmacionGuardado()
            }
        }

        btnFinal.setOnClickListener {
            if (registroExistente) {
                mostrarConfirmacionFinalizar()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar las coordenadas antes de finalizar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarCoordenadas() }
                )
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            obtenerUbicacionActual()
        }
    }

    private fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    latitudEditText.setText(lat.toString())
                    longitudEditText.setText(lon.toString())

                    val geoPoint = GeoPoint(lat, lon)
                    map.controller.setCenter(geoPoint)

                    map.overlays.removeAll { overlay -> overlay is Marker }
                    val marker = Marker(map).apply {
                        position = geoPoint
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Tu ubicación actual"
                    }
                    map.overlays.add(marker)
                    map.invalidate()
                } else {
                    mostrarDialogo(
                        titulo = "Ubicación no disponible",
                        mensaje = "No se pudo obtener tu ubicación actual",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF1976D2.toInt()
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
                titulo = "Validación",
                mensaje = "Debes seleccionar una ubicación en el mapa",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        val latValue = lat.toDoubleOrNull()
        val lonValue = lon.toDoubleOrNull()

        if (latValue == null || lonValue == null) {
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Las coordenadas deben ser valores numéricos válidos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        if (latValue < -90 || latValue > 90) {
            latitudEditText.error = "Latitud inválida (-90 a 90)"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "La latitud debe estar entre -90 y 90 grados",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        if (lonValue < -180 || lonValue > 180) {
            longitudEditText.error = "Longitud inválida (-180 a 180)"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "La longitud debe estar entre -180 y 180 grados",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun cargarDatosCoordenadas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosCoordenadas(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true
                        val latitud = datos.coordenadaY.toDoubleOrNull() ?: 19.4326
                        val longitud = datos.coordenadaX.toDoubleOrNull() ?: -99.1332

                        latitudEditText.setText(datos.coordenadaY)
                        longitudEditText.setText(datos.coordenadaX)

                        val point = GeoPoint(latitud, longitud)
                        map.overlays.removeAll { overlay -> overlay is Marker }
                        val marker = Marker(map).apply {
                            position = point
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Ubicación actual"
                        }
                        map.overlays.add(marker)
                        map.controller.setCenter(point)
                        map.invalidate()
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron coordenadas. Selecciona una nueva ubicación.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar estas coordenadas?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarCoordenadas() }
        )
    }

    private fun mostrarConfirmacionFinalizar() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea finalizar el proceso?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { irMenuAdministrador() }
        )
    }

    private fun guardarOActualizarCoordenadas() {
        val datos = datosCoordenadas(
            coordenadaX = longitudEditText.text.toString().trim(),
            coordenadaY = latitudEditText.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosCoordenadas(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosCoordenadas(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let {
                        registroExistente = true
                        mostrarDialogo(
                            titulo = "Éxito",
                            mensaje = if (registroExistente)
                                "Coordenadas actualizadas correctamente"
                            else
                                "Coordenadas guardadas correctamente",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF388E3C.toInt()
                        )
                    } ?: mostrarErrorServidor("Respuesta vacía del servidor")
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun irMenuAdministrador() {
        val intent = Intent(this, MenuAdministradorActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun manejarErrorRespuesta(codigo: Int, mensajeError: String?) {
        when (codigo) {
            404 -> mostrarDialogo(
                titulo = "No encontrado",
                mensaje = "El registro no fue encontrado",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            400 -> mostrarDialogo(
                titulo = "Datos inválidos",
                mensaje = "Verifique la información ingresada: ${mensajeError ?: "Error 400"}",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            500 -> mostrarErrorServidor("Error interno del servidor: ${mensajeError ?: "sin detalles"}")
            else -> mostrarErrorServidor("Error $codigo: ${mensajeError ?: "Error desconocido"}")
        }
    }

    private fun mostrarErrorServidor(mensaje: String) {
        mostrarDialogo(
            titulo = "Error del servidor",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorConexion(mensaje: String) {
        mostrarDialogo(
            titulo = "Error de conexión",
            mensaje = "No se pudo conectar al servidor: $mensaje",
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = "Ocurrió un error: $mensaje",
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual()
            } else {
                mostrarDialogo(
                    titulo = "Permiso denegado",
                    mensaje = "Se necesita permiso de ubicación para mostrar tu posición",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
            }
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
}