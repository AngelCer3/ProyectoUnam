package com.example.unamproject

data class Acreditado(
    val entidad_federativa : String,
    val ciudad_municipio_delegacion: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val nombres: String,
    val domicilio_calle: String,
    val domicilio_condominio: String,
    val domicilio_it: String,
    val domicilio_mz: String,
    val domicilio_no_ext: String,
    val domicilio_no_int: String,
    val domicilio_edif: String,
    val domicilio_colonia: String,
    val domicilio_cp: String,
    val domicilio_curp: String
)

data class Visitas(
    val visita1_fecha: String,
    val visita1_hora: String,
    val visita1_resultado: String,
    val visita2_fecha: String,
    val visita2_hora: String,
    val visita2_resultado: String,
    val visita3_fecha: String,
    val visita3_hora: String,
    val visita3_resultado: String
)
data class datosVivienda(
    val vivienda_localizada: String,
    val vivienda_habitada: String,
    val verificacion_metodo: String,
    val verificacion_otro: String,
    val vecino_nombre: String,
    val vecino_direccion: String,
    val acreditado_vive: String,
    val jefe_familia_nombre: String,
    val jefe_familia_relacion: String,
    val fecha_ocupacion: String,
    val situacion_vivienda: String,
    val documento_traspaso: String,
    val tipo_documento_traspaso: String,
    val documento_mostrado: String,
    val documento_copia_entregada: String
)