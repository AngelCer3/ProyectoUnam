package com.example.unamproject

import android.os.health.UidHealthStats
import java.io.Serializable

data class LoginRequest(
    val correo: String,
    val contrasena: String
)
data class LoginResponse(
    val usuario: UsuarioResponse
)

data class UsuarioResponse(
    val id_usuario: Int,
    val correo: String,
    val id_rol: Int
)

data class identificarAcreditado(
    val id_acreditado: String,
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
    val domicilio_curp: String,
    val id_usuario: String
): Serializable


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
    val domicilio_curp: String,
    val id_usuario: String
)
data class AcreditadoResponse(
    val success: Boolean,
    val id_acreditado: String
)
data class ResponseMessage(
    val success: Boolean,
    val message: String
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
    val visita3_resultado: String,
    val id_acreditado: String,
    val id_usuario: String
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
    val documento_copia_entregada: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosCredito(
    val credito_fecha_entrega: String,
    val credito_monto: String,
    val credito_sueldo_otorgado: String,
    val credito_fecha_ultimo_pago: String,
    val credito_recibo_pago: String,
    val credito_pago_actual: String,
    val credito_deuda_actual: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class DatosReestructura(
    val reestructura_motivo: String,
    val reestructura_documento: String,
    val reestructura_tipo_documento: String,
    val reestructura_solicitante_es_acred: String,
    val reestructura_solicitante_nombre: String,
    val reestructura_parentesco: String,
    val reestructura_motivo_personal: String,
    val reestructura_sexo: String,
    val reestructura_fecha_nacimiento: String,
    val reestructura_edad: String,
    val reestructura_lugar_nacimiento: String,
    val reestructura_grado_estudios: String,
    val reestructura_conocimiento_comp: String,
    val reestructura_discapacidad: String,
    val reestructura_dictamen: String,
    val reestructura_institucion_dictamen: String,
    val reestructura_fecha_dictamen: String,
    val reestructura_porcentaje_discapacidad: String,
    val reestructura_estado_civil: String,
    val reestructura_fecha_estado_civil: String,
    val reestructura_exesposo_aportacion: String,
    val reestructura_exesposo_monto: String,
    val reestructura_regimen_conyugal: String,
    val reestructura_vive_con_conyuge: String,
    val reestructura_fecha_no_convive: String,
    val id_acreditado: String,
    val id_usuario: String
)
data class datosConyuge(
    val conyuge_nombre: String,
    val conyuge_sexo: String,
    val conyuge_fecha_nacimiento: String,
    val conyuge_edad: String,
    val conyuge_grado_estudios: String,
    val conyuge_comp_computo: String,
    val id_acreditado: String,
    val id_usuario: String
)
data class datosFamiliares(
    val familia_integrantes: String,
    val familia_total_ocupantes: String,
    val familia_tipo: String,
    val edad_0_5_hombres: String,
    val edad_0_5_mujeres: String,
    val edad_6_12_hombres: String,
    val edad_6_12_mujeres: String,
    val edad_13_18_hombres: String,
    val edad_13_18_mujeres: String,
    val edad_19_35_hombres: String,
    val edad_19_35_mujeres: String,
    val edad_36_59_hombres: String,
    val edad_36_59_mujeres: String,
    val edad_60_mas_hombres: String,
    val edad_60_mas_mujeres: String,
    val escuela_asistencia: String,
    val escolaridad_niveles: String,
    val familiares_enfermedad: String,
    val familiares_enfermedad_cuantos: String,
    val familiares_enfermedad_quien: String,
    val comprobante_enfermedad: String,
    val tratamiento_recibido: String,
    val tratamiento_lugar: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosSolicitante(
    val hogar_integrantes_trabajando: String,
    val solicitante_activo: String,
    val solicitante_ocupacion_actual: String,
    val solicitante_desempleado_tiempo: String,
    val solicitante_empresa_previa: String,
    val solicitante_antiguedad_trabajo_anterior: String,
    val institucion_trabajo_solicitante: String,
    val actividad_remunerada_solicitante: String,
    val contrato_laboral_solicitante: String,
    val solicitante_ingreso_mensual: String,
    val solicitante_empresa: String,
    val solicitante_antiguedad: String,
    val comprobante_ingresos_solicitante: String,
    val institucion_cotizacion_solicitante: String,
    val ingresos_conceptos_solicitante: String,
    val id_acreditado: String,
    val id_usuario: String
)


data class datosEspecificosConyuge(
    val conyuge_activo: String,
    val conyuge_ocupacion_actual: String,
    val institucion_trabajo_conyuge: String,
    val conyuge_actividad_remunerada: String,
    val conyuge_contrato_laboral: String,
    val conyuge_ingreso_mensual: String,
    val conyuge_empresa: String,
    val conyuge_antiguedad: String,
    val comprobante_ingreso_conyuge: String,
    val institucion_cotizacion_conyuge: String,
    val ingresos_conceptos_conyuge: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosOtrosFamiliares(
    val otros_habitantes_actividad: String,
    val hijo_numero: String,
    val hijo_actividad: String,
    val hijo_aportacion: String,
    val padre_numero: String,
    val padre_actividad: String,
    val padre_aportacion: String,
    val madre_numero: String,
    val madre_actividad: String,
    val madre_aportacion: String,
    val suegros_numero: String,
    val suegros_actividad: String,
    val suegros_aportacion: String,
    val hermanos_numero: String,
    val hermanos_actividad: String,
    val hermanos_aportacion: String,
    val nietos_numeros: String,
    val nietos_actividad: String,
    val nietos_aportacion: String,
    val yernos_nueras_numero: String,
    val yernos_nueras_actividad: String,
    val yernos_nueras_aportacion: String,
    val otros_familiares_numero: String,
    val otros_familiares_actividad: String,
    val otros_familiares_aportacion: String,
    val no_familiares_numero: String,
    val no_familiares_actividad: String,
    val no_familiares_aportacion: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosGastos(
    val gasto_despensa_alimentacion: String,
    val gasto_despensa_motivo: String,
    val gasto_gas: String,
    val gasto_gas_motivo: String,
    val gasto_luz: String,
    val gasto_luz_motivo: String,
    val gasto_agua: String,
    val gasto_agua_motivo: String,
    val gasto_servicio_telefonico: String,
    val gasto_servicio_telefonico_motivo: String,
    val gasto_mantenimiento_vivienda: String,
    val gasto_mantenimiento_motivo: String,
    val gasto_transporte_publico: String,
    val gasto_transporte_motivo: String,
    val gasto_gasolina: String,
    val gasto_gasolina_motivo: String,
    val gasto_servicios_salud: String,
    val gasto_salud_motivo: String,
    val gasto_educacion: String,
    val gasto_educacion_motivo: String,
    val gasto_recreacion: String,
    val gasto_recreacion_motivo: String,
    val gasto_comidas_fuera: String,
    val gasto_comidas_fuera_motivo: String,
    val gasto_vestido_calzado: String,
    val gasto_vestido_calzado_motivo: String,
    val gasto_pension_vehiculo: String,
    val gasto_pension_vehiculo_motivo: String,
    val gasto_telefono_celular: String,
    val gasto_telefono_celular_motivo: String,
    val gasto_television_pago: String,
    val gasto_television_pago_motivo: String,
    val gasto_pago_creditos: String,
    val gasto_pago_creditos_motivo: String,
    val gasto_otros_descripcion: String,
    val gasto_otros_motivo: String,
    val gasto_metodo_pago: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosFamiliaDeudas(
    val familia_tiene_deudas: String,
    val familia_cantidad_deudas: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosTelefonos(
    val telefono1_lada: String,
    val telefono1_numero: String,
    val telefono1_extension: String,
    val telefono1_tipo: String,
    val telefono2_lada: String,
    val telefono2_numero: String,
    val telefono2_extension: String,
    val telefono2_tipo: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosCobranza(
    val cobranza_visita: String,
    val cobranza_numero_visitas: String,
    val cobranza_ultima_fecha_visita: String,
    val cobranza_despacho: String,
    val cobranza_calificacion: String,
    val cobranza_comentario: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosDocumentos(
    val doc_credencial_votar_cuenta: String,
    val doc_credencial_votar_mostro: String,
    val doc_credencial_votar_entrego_copia: String,
    val doc_poder_amplio_cuenta: String,
    val doc_poder_amplio_mostro: String,
    val doc_poder_amplio_entrego_copia: String,
    val doc_comprobante_ingresos_cuenta: String,
    val doc_comprobante_ingresos_mostro: String,
    val doc_comprobante_ingresos_entrego_copia: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosEspecificosVivienda(
    val vivienda_numero_habitaciones: String,
    val vivienda_tipo_piso: String,
    val vivienda_tipo_piso_otro: String,
    val vivienda_tipo_techo: String,
    val vivienda_cuenta_bano: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosObservaciones(
    val observaciones_entrevistador: String,
    val id_acreditado: String,
    val id_usuario: String
)

data class datosCoordenadas(
    val coordenadaX: String,
    val coordenadaY: String,
    val id_acreditado: String,
    val id_usuario: String
)