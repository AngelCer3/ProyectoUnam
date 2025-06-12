package com.example.unamproject.SinConexion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AcreditadoDao {
    @Insert
    suspend fun insertAcreditado(acreditado: AcreditadoEntity): Long

    @Query("SELECT * FROM acreditados WHERE id_acreditado = :id")
    suspend fun getAcreditadoById(id: String): AcreditadoEntity?

    @Query("SELECT * FROM acreditados")
    suspend fun getAllAcreditados(): List<AcreditadoEntity>

    @Update
    suspend fun updateAcreditado(acreditado: AcreditadoEntity)

    @Query("DELETE FROM acreditados WHERE id_acreditado = :id")
    suspend fun deleteAcreditado(id: String)

    @Query("DELETE FROM acreditados")
    suspend fun deleteAllAcreditados()
}

@Dao
interface VisitasDao {
    @Insert
    suspend fun insertVisita(visita: VisitasEntity)

    @Query("SELECT * FROM visitas WHERE id_acreditado = :idAcreditado")
    suspend fun getVisitasByAcreditado(idAcreditado: String): VisitasEntity?

    @Update
    suspend fun updateVisita(visita: VisitasEntity)

    @Query("DELETE FROM visitas WHERE id_acreditado = :idAcreditado")
    suspend fun deleteVisitasByAcreditado(idAcreditado: String)

    @Query("DELETE FROM visitas")
    suspend fun deleteAllVisitas()
}

@Dao
interface DatosViviendaDao {
    @Insert
    suspend fun insertDatosVivienda(datos: DatosViviendaEntity)

    @Query("SELECT * FROM datos_vivienda WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosViviendaByAcreditado(idAcreditado: String): DatosViviendaEntity?

    @Update
    suspend fun updateDatosVivienda(datos: DatosViviendaEntity)

    @Query("DELETE FROM datos_vivienda WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosViviendaByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_vivienda")
    suspend fun deleteAllDatosVivienda()
}

@Dao
interface DatosCreditoDao {
    @Insert
    suspend fun insertDatosCredito(datos: DatosCreditoEntity)

    @Query("SELECT * FROM datos_credito WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosCreditoByAcreditado(idAcreditado: String): DatosCreditoEntity?

    @Update
    suspend fun updateDatosCredito(datos: DatosCreditoEntity)

    @Query("DELETE FROM datos_credito WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosCreditoByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_credito")
    suspend fun deleteAllDatosCredito()
}

@Dao
interface DatosReestructuraDao {
    @Insert
    suspend fun insertDatosReestructura(datos: DatosReestructuraEntity)

    @Query("SELECT * FROM datos_reestructura WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosReestructuraByAcreditado(idAcreditado: String): DatosReestructuraEntity?

    @Update
    suspend fun updateDatosReestructura(datos: DatosReestructuraEntity)

    @Query("DELETE FROM datos_reestructura WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosReestructuraByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_reestructura")
    suspend fun deleteAllDatosReestructura()
}

@Dao
interface DatosConyugeDao {
    @Insert
    suspend fun insertDatosConyuge(datos: DatosConyugeEntity)

    @Query("SELECT * FROM datos_conyuge WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosConyugeByAcreditado(idAcreditado: String): DatosConyugeEntity?

    @Update
    suspend fun updateDatosConyuge(datos: DatosConyugeEntity)

    @Query("DELETE FROM datos_conyuge WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosConyugeByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_conyuge")
    suspend fun deleteAllDatosConyuge()
}

@Dao
interface DatosFamiliaresDao {
    @Insert
    suspend fun insertDatosFamiliares(datos: DatosFamiliaresEntity)

    @Query("SELECT * FROM datos_familiares WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosFamiliaresByAcreditado(idAcreditado: String): DatosFamiliaresEntity?

    @Update
    suspend fun updateDatosFamiliares(datos: DatosFamiliaresEntity)

    @Query("DELETE FROM datos_familiares WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosFamiliaresByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_familiares")
    suspend fun deleteAllDatosFamiliares()
}

@Dao
interface DatosSolicitanteDao {
    @Insert
    suspend fun insertDatosSolicitante(datos: DatosSolicitanteEntity)

    @Query("SELECT * FROM datos_solicitante WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosSolicitanteByAcreditado(idAcreditado: String): DatosSolicitanteEntity?

    @Update
    suspend fun updateDatosSolicitante(datos: DatosSolicitanteEntity)

    @Query("DELETE FROM datos_solicitante WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosSolicitanteByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_solicitante")
    suspend fun deleteAllDatosSolicitante()
}

@Dao
interface DatosEspecificosConyugeDao {
    @Insert
    suspend fun insertDatosEspecificosConyuge(datos: DatosEspecificosConyugeEntity)

    @Query("SELECT * FROM datos_especificos_conyuge WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosEspecificosConyugeByAcreditado(idAcreditado: String): DatosEspecificosConyugeEntity?

    @Update
    suspend fun updateDatosEspecificosConyuge(datos: DatosEspecificosConyugeEntity)

    @Query("DELETE FROM datos_especificos_conyuge WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosEspecificosConyugeByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_especificos_conyuge")
    suspend fun deleteAllDatosEspecificosConyuge()
}

@Dao
interface DatosOtrosFamiliaresDao {
    @Insert
    suspend fun insertDatosOtrosFamiliares(datos: DatosOtrosFamiliaresEntity)

    @Query("SELECT * FROM datos_otros_familiares WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosOtrosFamiliaresByAcreditado(idAcreditado: String): DatosOtrosFamiliaresEntity?

    @Update
    suspend fun updateDatosOtrosFamiliares(datos: DatosOtrosFamiliaresEntity)

    @Query("DELETE FROM datos_otros_familiares WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosOtrosFamiliaresByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_otros_familiares")
    suspend fun deleteAllDatosOtrosFamiliares()
}

@Dao
interface DatosGastosDao {
    @Insert
    suspend fun insertDatosGastos(datos: DatosGastosEntity)

    @Query("SELECT * FROM datos_gastos WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosGastosByAcreditado(idAcreditado: String): DatosGastosEntity?

    @Update
    suspend fun updateDatosGastos(datos: DatosGastosEntity)

    @Query("DELETE FROM datos_gastos WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosGastosByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_gastos")
    suspend fun deleteAllDatosGastos()
}

@Dao
interface DatosFamiliaDeudasDao {
    @Insert
    suspend fun insertDatosFamiliaDeudas(datos: DatosFamiliaDeudasEntity)

    @Query("SELECT * FROM datos_familia_deudas WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosFamiliaDeudasByAcreditado(idAcreditado: String): DatosFamiliaDeudasEntity?

    @Update
    suspend fun updateDatosFamiliaDeudas(datos: DatosFamiliaDeudasEntity)

    @Query("DELETE FROM datos_familia_deudas WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosFamiliaDeudasByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_familia_deudas")
    suspend fun deleteAllDatosFamiliaDeudas()
}

@Dao
interface DatosTelefonosDao {
    @Insert
    suspend fun insertDatosTelefonos(datos: DatosTelefonosEntity)

    @Query("SELECT * FROM datos_telefonos WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosTelefonosByAcreditado(idAcreditado: String): DatosTelefonosEntity?

    @Update
    suspend fun updateDatosTelefonos(datos: DatosTelefonosEntity)

    @Query("DELETE FROM datos_telefonos WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosTelefonosByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_telefonos")
    suspend fun deleteAllDatosTelefonos()
}

@Dao
interface DatosCobranzaDao {
    @Insert
    suspend fun insertDatosCobranza(datos: DatosCobranzaEntity)

    @Query("SELECT * FROM datos_cobranza WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosCobranzaByAcreditado(idAcreditado: String): DatosCobranzaEntity?

    @Update
    suspend fun updateDatosCobranza(datos: DatosCobranzaEntity)

    @Query("DELETE FROM datos_cobranza WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosCobranzaByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_cobranza")
    suspend fun deleteAllDatosCobranza()
}

@Dao
interface DatosDocumentosDao {
    @Insert
    suspend fun insertDatosDocumentos(datos: DatosDocumentosEntity)

    @Query("SELECT * FROM datos_documentos WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosDocumentosByAcreditado(idAcreditado: String): DatosDocumentosEntity?

    @Update
    suspend fun updateDatosDocumentos(datos: DatosDocumentosEntity)

    @Query("DELETE FROM datos_documentos WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosDocumentosByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_documentos")
    suspend fun deleteAllDatosDocumentos()
}

@Dao
interface DatosEspecificosViviendaDao {
    @Insert
    suspend fun insertDatosEspecificosVivienda(datos: DatosEspecificosViviendaEntity)

    @Query("SELECT * FROM datos_especificos_vivienda WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosEspecificosViviendaByAcreditado(idAcreditado: String): DatosEspecificosViviendaEntity?

    @Update
    suspend fun updateDatosEspecificosVivienda(datos: DatosEspecificosViviendaEntity)

    @Query("DELETE FROM datos_especificos_vivienda WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosEspecificosViviendaByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_especificos_vivienda")
    suspend fun deleteAllDatosEspecificosVivienda()
}

@Dao
interface DatosObservacionesDao {
    @Insert
    suspend fun insertDatosObservaciones(datos: DatosObservacionesEntity)

    @Query("SELECT * FROM datos_observaciones WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosObservacionesByAcreditado(idAcreditado: String): DatosObservacionesEntity?

    @Update
    suspend fun updateDatosObservaciones(datos: DatosObservacionesEntity)

    @Query("DELETE FROM datos_observaciones WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosObservacionesByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_observaciones")
    suspend fun deleteAllDatosObservaciones()
}

@Dao
interface DatosCoordenadasDao {
    @Insert
    suspend fun insertDatosCoordenadas(datos: DatosCoordenadasEntity)

    @Query("SELECT * FROM datos_coordenadas WHERE id_acreditado = :idAcreditado")
    suspend fun getDatosCoordenadasByAcreditado(idAcreditado: String): DatosCoordenadasEntity?

    @Update
    suspend fun updateDatosCoordenadas(datos: DatosCoordenadasEntity)

    @Query("DELETE FROM datos_coordenadas WHERE id_acreditado = :idAcreditado")
    suspend fun deleteDatosCoordenadasByAcreditado(idAcreditado: String)

    @Query("DELETE FROM datos_coordenadas")
    suspend fun deleteAllDatosCoordenadas()
}