package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte10SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var idAcreditado: String

    // EditTexts
    private lateinit var otrosHabitantesActividadEt: EditText
    private lateinit var hijoNumeroEt: EditText
    private lateinit var hijoActividadEt: EditText
    private lateinit var hijoAportacionEt: EditText
    private lateinit var padreNumeroEt: EditText
    private lateinit var padreActividadEt: EditText
    private lateinit var padreAportacionEt: EditText
    private lateinit var madreNumeroEt: EditText
    private lateinit var madreActividadEt: EditText
    private lateinit var madreAportacionEt: EditText
    private lateinit var suegrosNumeroEt: EditText
    private lateinit var suegrosActividadEt: EditText
    private lateinit var suegrosAportacionEt: EditText
    private lateinit var hermanosNumeroEt: EditText
    private lateinit var hermanosActividadEt: EditText
    private lateinit var hermanosAportacionEt: EditText
    private lateinit var nietosNumerosEt: EditText
    private lateinit var nietosActividadEt: EditText
    private lateinit var nietosAportacionEt: EditText
    private lateinit var yernosNuerasNumeroEt: EditText
    private lateinit var yernosNuerasActividadEt: EditText
    private lateinit var yernosNuerasAportacionEt: EditText
    private lateinit var otrosFamiliaresNumeroEt: EditText
    private lateinit var otrosFamiliaresActividadEt: EditText
    private lateinit var otrosFamiliaresAportacionEt: EditText
    private lateinit var noFamiliaresNumeroEt: EditText
    private lateinit var noFamiliaresActividadEt: EditText
    private lateinit var noFamiliaresAportacionEt: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte10_sin_conexion)

        idAcreditado = intent.getStringExtra("id_acreditado") ?: ""
        database = AppDatabase.getDatabase(this)

        // Inicializar EditTexts
        otrosHabitantesActividadEt = findViewById(R.id.otros_habitantes_actividad)
        hijoNumeroEt = findViewById(R.id.hijo_numero)
        hijoActividadEt = findViewById(R.id.hijo_actividad)
        hijoAportacionEt = findViewById(R.id.hijo_aportacion)
        padreNumeroEt = findViewById(R.id.padre_numero)
        padreActividadEt = findViewById(R.id.padre_actividad)
        padreAportacionEt = findViewById(R.id.padre_aportacion)
        madreNumeroEt = findViewById(R.id.madre_numero)
        madreActividadEt = findViewById(R.id.madre_actividad)
        madreAportacionEt = findViewById(R.id.madre_aportacion)
        suegrosNumeroEt = findViewById(R.id.suegros_numero)
        suegrosActividadEt = findViewById(R.id.suegros_actividad)
        suegrosAportacionEt = findViewById(R.id.suegros_aportacion)
        hermanosNumeroEt = findViewById(R.id.hermanos_numero)
        hermanosActividadEt = findViewById(R.id.hermanos_actividad)
        hermanosAportacionEt = findViewById(R.id.hermanos_aportacion)
        nietosNumerosEt = findViewById(R.id.nietos_numeros)
        nietosActividadEt = findViewById(R.id.nietos_actividad)
        nietosAportacionEt = findViewById(R.id.nietos_aportacion)
        yernosNuerasNumeroEt = findViewById(R.id.yernos_nueras_numero)
        yernosNuerasActividadEt = findViewById(R.id.yernos_nueras_actividad)
        yernosNuerasAportacionEt = findViewById(R.id.yernos_nueras_aportacion)
        otrosFamiliaresNumeroEt = findViewById(R.id.otros_familiares_numero)
        otrosFamiliaresActividadEt = findViewById(R.id.otros_familiares_actividad)
        otrosFamiliaresAportacionEt = findViewById(R.id.otros_familiares_aportacion)
        noFamiliaresNumeroEt = findViewById(R.id.no_familiares_numero)
        noFamiliaresActividadEt = findViewById(R.id.no_familiares_actividad)
        noFamiliaresAportacionEt = findViewById(R.id.no_familiares_aportacion)

        // Botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        btnGuardar.setOnClickListener {
            guardarDatosOtrosFamiliares()
        }

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Formatoparte11SinConexion::class.java)
            intent.putExtra("id_acreditado", idAcreditado)
            startActivity(intent)
        }
    }

    private fun guardarDatosOtrosFamiliares() {
        val datos = DatosOtrosFamiliaresEntity(
            otros_habitantes_actividad = otrosHabitantesActividadEt.text.toString(),
            hijo_numero = hijoNumeroEt.text.toString(),
            hijo_actividad = hijoActividadEt.text.toString(),
            hijo_aportacion = hijoAportacionEt.text.toString(),
            padre_numero = padreNumeroEt.text.toString(),
            padre_actividad = padreActividadEt.text.toString(),
            padre_aportacion = padreAportacionEt.text.toString(),
            madre_numero = madreNumeroEt.text.toString(),
            madre_actividad = madreActividadEt.text.toString(),
            madre_aportacion = madreAportacionEt.text.toString(),
            suegros_numero = suegrosNumeroEt.text.toString(),
            suegros_actividad = suegrosActividadEt.text.toString(),
            suegros_aportacion = suegrosAportacionEt.text.toString(),
            hermanos_numero = hermanosNumeroEt.text.toString(),
            hermanos_actividad = hermanosActividadEt.text.toString(),
            hermanos_aportacion = hermanosAportacionEt.text.toString(),
            nietos_numeros = nietosNumerosEt.text.toString(),
            nietos_actividad = nietosActividadEt.text.toString(),
            nietos_aportacion = nietosAportacionEt.text.toString(),
            yernos_nueras_numero = yernosNuerasNumeroEt.text.toString(),
            yernos_nueras_actividad = yernosNuerasActividadEt.text.toString(),
            yernos_nueras_aportacion = yernosNuerasAportacionEt.text.toString(),
            otros_familiares_numero = otrosFamiliaresNumeroEt.text.toString(),
            otros_familiares_actividad = otrosFamiliaresActividadEt.text.toString(),
            otros_familiares_aportacion = otrosFamiliaresAportacionEt.text.toString(),
            no_familiares_numero = noFamiliaresNumeroEt.text.toString(),
            no_familiares_actividad = noFamiliaresActividadEt.text.toString(),
            no_familiares_aportacion = noFamiliaresAportacionEt.text.toString(),
            id_acreditado = idAcreditado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosOtrosFamiliaresDao().insertDatosOtrosFamiliares(datos)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte10SinConexion, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Formatoparte10SinConexion, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
