package com.example.unamproject.SinConexion

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AcreditadoEntity::class,
        VisitasEntity::class,
        DatosViviendaEntity::class,
        DatosCreditoEntity::class,
        DatosReestructuraEntity::class,
        DatosConyugeEntity::class,
        DatosFamiliaresEntity::class,
        DatosSolicitanteEntity::class,
        DatosEspecificosConyugeEntity::class,
        DatosOtrosFamiliaresEntity::class,
        DatosGastosEntity::class,
        DatosFamiliaDeudasEntity::class,
        DatosTelefonosEntity::class,
        DatosCobranzaEntity::class,
        DatosDocumentosEntity::class,
        DatosEspecificosViviendaEntity::class,
        DatosObservacionesEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun acreditadoDao(): AcreditadoDao
    abstract fun visitasDao(): VisitasDao
    abstract fun datosViviendaDao(): DatosViviendaDao
    abstract fun datosCreditoDao(): DatosCreditoDao
    abstract fun datosReestructuraDao(): DatosReestructuraDao
    abstract fun datosConyugeDao(): DatosConyugeDao
    abstract fun datosFamiliaresDao(): DatosFamiliaresDao
    abstract fun datosSolicitanteDao(): DatosSolicitanteDao
    abstract fun datosEspecificosConyugeDao(): DatosEspecificosConyugeDao
    abstract fun datosOtrosFamiliaresDao(): DatosOtrosFamiliaresDao
    abstract fun datosGastosDao(): DatosGastosDao
    abstract fun datosFamiliaDeudasDao(): DatosFamiliaDeudasDao
    abstract fun datosTelefonosDao(): DatosTelefonosDao
    abstract fun datosCobranzaDao(): DatosCobranzaDao
    abstract fun datosDocumentosDao(): DatosDocumentosDao
    abstract fun datosEspecificosViviendaDao(): DatosEspecificosViviendaDao
    abstract fun datosObservacionesDao(): DatosObservacionesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unam_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}