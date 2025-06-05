package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosOtrosFamiliares
import kotlinx.coroutines.launch

class actualizarFormatoparte10 : AppCompatActivity() {

    private lateinit var otrosHabitantesActividad: EditText
    private lateinit var hijoNumero: EditText
    private lateinit var hijoActividad: EditText
    private lateinit var hijoAportacion: EditText
    private lateinit var padreNumero: EditText
    private lateinit var padreActividad: EditText
    private lateinit var padreAportacion: EditText
    private lateinit var madreNumero: EditText
    private lateinit var madreActividad: EditText
    private lateinit var madreAportacion: EditText
    private lateinit var suegrosNumero: EditText
    private lateinit var suegrosActividad: EditText
    private lateinit var suegrosAportacion: EditText
    private lateinit var hermanosNumero: EditText
    private lateinit var hermanosActividad: EditText
    private lateinit var hermanosAportacion: EditText
    private lateinit var nietosNumeros: EditText
    private lateinit var nietosActividad: EditText
    private lateinit var nietosAportacion: EditText
    private lateinit var yernosNuerasNumero: EditText
    private lateinit var yernosNuerasActividad: EditText
    private lateinit var yernosNuerasAportacion: EditText
    private lateinit var otrosFamiliaresNumero: EditText
    private lateinit var otrosFamiliaresActividad: EditText
    private lateinit var otrosFamiliaresAportacion: EditText
    private lateinit var noFamiliaresNumero: EditText
    private lateinit var noFamiliaresActividad: EditText
    private lateinit var noFamiliaresAportacion: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_formatoparte10)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind EditTexts
        otrosHabitantesActividad = findViewById(R.id.otros_habitantes_actividad)
        hijoNumero = findViewById(R.id.hijo_numero)
        hijoActividad = findViewById(R.id.hijo_actividad)
        hijoAportacion = findViewById(R.id.hijo_aportacion)
        padreNumero = findViewById(R.id.padre_numero)
        padreActividad = findViewById(R.id.padre_actividad)
        padreAportacion = findViewById(R.id.padre_aportacion)
        madreNumero = findViewById(R.id.madre_numero)
        madreActividad = findViewById(R.id.madre_actividad)
        madreAportacion = findViewById(R.id.madre_aportacion)
        suegrosNumero = findViewById(R.id.suegros_numero)
        suegrosActividad = findViewById(R.id.suegros_actividad)
        suegrosAportacion = findViewById(R.id.suegros_aportacion)
        hermanosNumero = findViewById(R.id.hermanos_numero)
        hermanosActividad = findViewById(R.id.hermanos_actividad)
        hermanosAportacion = findViewById(R.id.hermanos_aportacion)
        nietosNumeros = findViewById(R.id.nietos_numeros)
        nietosActividad = findViewById(R.id.nietos_actividad)
        nietosAportacion = findViewById(R.id.nietos_aportacion)
        yernosNuerasNumero = findViewById(R.id.yernos_nueras_numero)
        yernosNuerasActividad = findViewById(R.id.yernos_nueras_actividad)
        yernosNuerasAportacion = findViewById(R.id.yernos_nueras_aportacion)
        otrosFamiliaresNumero = findViewById(R.id.otros_familiares_numero)
        otrosFamiliaresActividad = findViewById(R.id.otros_familiares_actividad)
        otrosFamiliaresAportacion = findViewById(R.id.otros_familiares_aportacion)
        noFamiliaresNumero = findViewById(R.id.no_familiares_numero)
        noFamiliaresActividad = findViewById(R.id.no_familiares_actividad)
        noFamiliaresAportacion = findViewById(R.id.no_familiares_aportacion)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        if (idAcreditado.isEmpty()) {
            Toast.makeText(this, "ID acreditado no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cargarDatosOtrosFamiliares()

        btnActualizarDatos.setOnClickListener {
            actualizarDatosOtrosFamiliares()
        }

        btnSiguiente.setOnClickListener {
            irASiguiente()
        }
    }

    private fun cargarDatosOtrosFamiliares() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosOtrosFamiliares(idAcreditado)
                if (response.isSuccessful && response.body() != null) {
                    val datos = response.body()!!
                    otrosHabitantesActividad.setText(datos.otros_habitantes_actividad)
                    hijoNumero.setText(datos.hijo_numero)
                    hijoActividad.setText(datos.hijo_actividad)
                    hijoAportacion.setText(datos.hijo_aportacion)
                    padreNumero.setText(datos.padre_numero)
                    padreActividad.setText(datos.padre_actividad)
                    padreAportacion.setText(datos.padre_aportacion)
                    madreNumero.setText(datos.madre_numero)
                    madreActividad.setText(datos.madre_actividad)
                    madreAportacion.setText(datos.madre_aportacion)
                    suegrosNumero.setText(datos.suegros_numero)
                    suegrosActividad.setText(datos.suegros_actividad)
                    suegrosAportacion.setText(datos.suegros_aportacion)
                    hermanosNumero.setText(datos.hermanos_numero)
                    hermanosActividad.setText(datos.hermanos_actividad)
                    hermanosAportacion.setText(datos.hermanos_aportacion)
                    nietosNumeros.setText(datos.nietos_numeros)
                    nietosActividad.setText(datos.nietos_actividad)
                    nietosAportacion.setText(datos.nietos_aportacion)
                    yernosNuerasNumero.setText(datos.yernos_nueras_numero)
                    yernosNuerasActividad.setText(datos.yernos_nueras_actividad)
                    yernosNuerasAportacion.setText(datos.yernos_nueras_aportacion)
                    otrosFamiliaresNumero.setText(datos.otros_familiares_numero)
                    otrosFamiliaresActividad.setText(datos.otros_familiares_actividad)
                    otrosFamiliaresAportacion.setText(datos.otros_familiares_aportacion)
                    noFamiliaresNumero.setText(datos.no_familiares_numero)
                    noFamiliaresActividad.setText(datos.no_familiares_actividad)
                    noFamiliaresAportacion.setText(datos.no_familiares_aportacion)
                } else {
                    Toast.makeText(this@actualizarFormatoparte10, "No se encontraron datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte10, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun actualizarDatosOtrosFamiliares() {
        val datos = datosOtrosFamiliares(
            otros_habitantes_actividad = otrosHabitantesActividad.text.toString(),
            hijo_numero = hijoNumero.text.toString(),
            hijo_actividad = hijoActividad.text.toString(),
            hijo_aportacion = hijoAportacion.text.toString(),
            padre_numero = padreNumero.text.toString(),
            padre_actividad = padreActividad.text.toString(),
            padre_aportacion = padreAportacion.text.toString(),
            madre_numero = madreNumero.text.toString(),
            madre_actividad = madreActividad.text.toString(),
            madre_aportacion = madreAportacion.text.toString(),
            suegros_numero = suegrosNumero.text.toString(),
            suegros_actividad = suegrosActividad.text.toString(),
            suegros_aportacion = suegrosAportacion.text.toString(),
            hermanos_numero = hermanosNumero.text.toString(),
            hermanos_actividad = hermanosActividad.text.toString(),
            hermanos_aportacion = hermanosAportacion.text.toString(),
            nietos_numeros = nietosNumeros.text.toString(),
            nietos_actividad = nietosActividad.text.toString(),
            nietos_aportacion = nietosAportacion.text.toString(),
            yernos_nueras_numero = yernosNuerasNumero.text.toString(),
            yernos_nueras_actividad = yernosNuerasActividad.text.toString(),
            yernos_nueras_aportacion = yernosNuerasAportacion.text.toString(),
            otros_familiares_numero = otrosFamiliaresNumero.text.toString(),
            otros_familiares_actividad = otrosFamiliaresActividad.text.toString(),
            otros_familiares_aportacion = otrosFamiliaresAportacion.text.toString(),
            no_familiares_numero = noFamiliaresNumero.text.toString(),
            no_familiares_actividad = noFamiliaresActividad.text.toString(),
            no_familiares_aportacion = noFamiliaresAportacion.text.toString(),
            id_acreditado = idAcreditado
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosOtrosFamiliares(idAcreditado, datos)
                if (response.isSuccessful) {
                    Toast.makeText(this@actualizarFormatoparte10, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@actualizarFormatoparte10, "Error al actualizar datos", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@actualizarFormatoparte10, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte11::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        startActivity(intent)
    }
}
