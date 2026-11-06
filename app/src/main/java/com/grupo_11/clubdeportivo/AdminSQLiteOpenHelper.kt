package com.grupo_11.clubdeportivo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Habilita el control de las relaciones entre las tablas.
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crea las tablas de la primera versión,
        // conservadas temporalmente hasta adaptar los métodos al nuevo modelo SQLite.
        db?.execSQL(CREATE_TABLE_USUARIOS)
        db?.execSQL(CREATE_TABLE_SOCIOS)
        db?.execSQL(CREATE_TABLE_NO_SOCIOS)
        db?.execSQL(CREATE_TABLE_PAGOS)
        // Carga un usuario por defecto para el empleado administrativo
        db?.execSQL("INSERT INTO usuarios (usuario, contrasena) VALUES ('admin', '1234')")


        // Tablas del nuevo modelo SQLite
        db?.execSQL(CREATE_TABLE_PERSONA)
        db?.execSQL(CREATE_TABLE_SOCIO)
        db?.execSQL(CREATE_TABLE_ACTIVIDAD)
        db?.execSQL(CREATE_TABLE_HORARIO_ACTIVIDAD)
        db?.execSQL(CREATE_TABLE_VISITA)
        db?.execSQL(CREATE_TABLE_CUOTA)
        db?.execSQL(CREATE_TABLE_PAGO)
        db?.execSQL(CREATE_TABLE_VALOR_CUOTA_MENSUAL)

        // Carga el valor inicial de la cuota mensual.
        db?.execSQL("""
            INSERT INTO valor_cuota_mensual (importe, fechaVigencia)
            VALUES (45000.00, '2026-11-01')
        """.trimIndent())


        // Carga las actividades con importes de ejemplo actualizados.
        db?.execSQL("""
            INSERT INTO actividad (idActividad, nombre, profesor, importe)
            VALUES
                (1, 'Futbol Infantil', 'Juan Perez', 9000.00),
                (2, 'Futbol Juvenil', 'Carlos Gomez', 10000.00),
                (3, 'Basquet', 'Martin Fernandez', 9000.00),
                (4, 'Voley', 'Lucia Rodriguez', 9000.00),
                (5, 'Taekwondo', 'Diego Martinez', 12000.00),
                (6, 'Patin Artistico', 'Sofia Lopez', 11000.00),
                (7, 'Gimnasia Funcional', 'Mariana Diaz', 8000.00),
                (8, 'Zumba', 'Carla Romero', 8000.00)
        """.trimIndent())


        // Carga los horarios del proyecto original asociados a cada actividad.
        db?.execSQL("""
            INSERT INTO horarioactividad
                (idHorario, idActividad, diaSemana, horaInicio, horaFin)
            VALUES
                (1, 1, 'Martes', '18:00', '19:30'),
                (2, 1, 'Jueves', '18:00', '19:30'),
                (3, 2, 'Martes', '20:00', '21:30'),
                (4, 2, 'Jueves', '20:00', '21:30'),
                (5, 3, 'Lunes', '19:00', '20:30'),
                (6, 3, 'Miércoles', '19:00', '20:30'),
                (7, 4, 'Martes', '19:00', '20:30'),
                (8, 4, 'Viernes', '19:00', '20:30'),
                (9, 5, 'Martes', '20:00', '21:00'),
                (10, 5, 'Jueves', '20:00', '21:00'),
                (11, 6, 'Miércoles', '18:00', '19:30'),
                (12, 6, 'Viernes', '18:00', '19:30'),
                (13, 7, 'Lunes', '08:00', '09:00'),
                (14, 7, 'Miércoles', '08:00', '09:00'),
                (15, 7, 'Viernes', '08:00', '09:00'),
                (16, 8, 'Martes', '09:00', '10:00'),
                (17, 8, 'Jueves', '09:00', '10:00')
        """.trimIndent())

        // Carga los datos ficticios para probar el reporte de vencimientos.
        db?.let { DatosPruebaSQLite.cargarDatosPrueba(it) }

    }


    // Actualiza la base de datos sin eliminar las tablas existentes.
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            db?.execSQL(CREATE_TABLE_PERSONA)
            db?.execSQL(CREATE_TABLE_SOCIO)
            db?.execSQL(CREATE_TABLE_CUOTA)
            db?.execSQL(CREATE_TABLE_ACTIVIDAD)
            db?.execSQL(CREATE_TABLE_HORARIO_ACTIVIDAD)
            db?.execSQL(CREATE_TABLE_VISITA)
            db?.execSQL(CREATE_TABLE_PAGO)
            db?.execSQL(CREATE_TABLE_VALOR_CUOTA_MENSUAL)


            // Carga el importe inicial si todavía no existe.
            db?.execSQL("""
                INSERT OR IGNORE INTO valor_cuota_mensual (importe, fechaVigencia)
                VALUES (45000.00, '2026-11-01')
            """.trimIndent())

            // Carga las actividades al actualizar la base de datos.
            db?.execSQL("""
                INSERT OR IGNORE INTO actividad (idActividad, nombre, profesor, importe)
                VALUES
                    (1, 'Futbol Infantil', 'Juan Perez', 9000.00),
                    (2, 'Futbol Juvenil', 'Carlos Gomez', 10000.00),
                    (3, 'Basquet', 'Martin Fernandez', 9000.00),
                    (4, 'Voley', 'Lucia Rodriguez', 9000.00),
                    (5, 'Taekwondo', 'Diego Martinez', 12000.00),
                    (6, 'Patin Artistico', 'Sofia Lopez', 11000.00),
                    (7, 'Gimnasia Funcional', 'Mariana Diaz', 8000.00),
                    (8, 'Zumba', 'Carla Romero', 8000.00)
            """.trimIndent())


            // Carga los horarios asociados a las actividades.
            db?.execSQL("""
                INSERT OR IGNORE INTO horarioactividad
                    (idHorario, idActividad, diaSemana, horaInicio, horaFin)
                VALUES
                    (1, 1, 'Martes', '18:00', '19:30'),
                    (2, 1, 'Jueves', '18:00', '19:30'),
                    (3, 2, 'Martes', '20:00', '21:30'),
                    (4, 2, 'Jueves', '20:00', '21:30'),
                    (5, 3, 'Lunes', '19:00', '20:30'),
                    (6, 3, 'Miércoles', '19:00', '20:30'),
                    (7, 4, 'Martes', '19:00', '20:30'),
                    (8, 4, 'Viernes', '19:00', '20:30'),
                    (9, 5, 'Martes', '20:00', '21:00'),
                    (10, 5, 'Jueves', '20:00', '21:00'),
                    (11, 6, 'Miércoles', '18:00', '19:30'),
                    (12, 6, 'Viernes', '18:00', '19:30'),
                    (13, 7, 'Lunes', '08:00', '09:00'),
                    (14, 7, 'Miércoles', '08:00', '09:00'),
                    (15, 7, 'Viernes', '08:00', '09:00'),
                    (16, 8, 'Martes', '09:00', '10:00'),
                    (17, 8, 'Jueves', '09:00', '10:00')
            """.trimIndent())

            // Carga los datos ficticios al actualizar la base desde la versión anterior.
            db?.let { DatosPruebaSQLite.cargarDatosPrueba(it) }


        }
    }


    /** Verifica si las credenciales de usuario y contraseña existen en SQLite */
    fun validarUsuario(usuario: String, contrasena: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, contrasena)
        )
        val esValido = cursor.count > 0
        cursor.close()
        return esValido
    }

    // =========================================================================
    // MÉTODOS DE VALIDACIÓN DE UNICIDAD (DNI y Alias)
    // =========================================================================

    /** Verifica si un DNI ya pertenece a un socio registrado */
    fun existeDniSocio(dni: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM socios WHERE dni = ?", arrayOf(dni))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    /** Verifica si un Alias ya está en uso por otro socio */
    fun existeAliasSocio(alias: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM socios WHERE alias = ?", arrayOf(alias))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    /** Verifica si un DNI ya pertenece a un no socio */
    fun existeDniNoSocio(dni: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM no_socios WHERE dni = ?", arrayOf(dni))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }

    // =========================================================================
    // MÉTODOS DE INSERCIÓN (CREAR REGISTROS)
    // =========================================================================


    /** Registra una nueva visita y crea la persona si todavía no existe */
    fun registrarVisita(
        dni: String,
        nombre: String,
        apellido: String,
        telefono: String,
        aptoFisico: Boolean,
        idHorario: Int,
        fechaAsistencia: String
    ): Long {
        val db = writableDatabase

        try {
            db.beginTransaction()

            // Comprobar si la persona ya está registrada y si presentó el apto físico
            val cursor = db.rawQuery(
                "SELECT aptoFisico FROM persona WHERE dni = ?",
                arrayOf(dni)
            )

            var personaExiste = false
            var tieneApto = false

            cursor.use {
                if (it.moveToFirst()) {
                    personaExiste = true
                    tieneApto = it.getInt(0) == 1
                }
            }

            if (!personaExiste) {
                // Para dar de alta a una persona nueva, el apto físico es obligatorio
                if (!aptoFisico) return -1L

                // Guardar la fecha de alta de la persona
                val fechaAlta = java.text.SimpleDateFormat(
                    "yyyy-MM-dd",
                    java.util.Locale.US
                ).format(java.util.Date())

                val datosPersona = ContentValues().apply {
                    put("dni", dni)
                    put("nombre", nombre)
                    put("apellido", apellido)
                    put("telefono", telefono)
                    put("aptoFisico", 1)
                    put("fechaAlta", fechaAlta)
                }

                db.insertOrThrow("persona", null, datosPersona)
            } else {
                // Si ya existe, reutilizar el apto físico registrado
                if (!tieneApto) return -1L
            }

            // Obtener el importe de la actividad asociada al horario elegido
            val cursorImporte = db.rawQuery(
                """
                SELECT actividad.importe
                FROM horarioactividad
                INNER JOIN actividad
                    ON horarioactividad.idActividad = actividad.idActividad
                WHERE horarioactividad.idHorario = ?
                """.trimIndent(),
                arrayOf(idHorario.toString())
            )

            val importe = cursorImporte.use {
                if (it.moveToFirst()) it.getDouble(0) else return -1L
            }

            val fechaActual = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.US
            ).format(java.util.Date())

            // Registrar una nueva visita, aunque ese DNI tenga visitas anteriores
            val datosVisita = ContentValues().apply {
                put("persona_dni", dni)
                put("idHorario", idHorario)
                put("fechaAsistencia", fechaAsistencia)
                put("fechaRegistro", fechaActual)
                put("importe", importe)
                put("pagada", 0)
            }

            val idVisita = db.insertOrThrow("visita", null, datosVisita)

            db.setTransactionSuccessful()
            return idVisita

        } catch (e: android.database.SQLException) {
            return -1L
        } finally {
            if (db.inTransaction()) {
                db.endTransaction()
            }
        }
    }


    /** Registra un socio y genera su primera cuota mensual */
    fun registrarSocio(
        dni: String,
        nombre: String,
        apellido: String,
        telefono: String,
        aptoFisico: Boolean
    ): Long {
        val db = writableDatabase

        try {
            db.beginTransaction()

            // Obtener la fecha actual
            val formato = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.US
            )
            val fechaAlta = formato.format(java.util.Date())

            // Buscar si la persona ya está registrada
            val cursorPersona = db.rawQuery(
                "SELECT aptoFisico FROM persona WHERE dni = ?",
                arrayOf(dni)
            )

            var personaExiste = false
            var tieneApto = false

            cursorPersona.use {
                if (it.moveToFirst()) {
                    personaExiste = true
                    tieneApto = it.getInt(0) == 1
                }
            }

            if (!personaExiste) {
                // Para un alta nueva, el apto físico es obligatorio
                if (!aptoFisico) return -1L

                val datosPersona = ContentValues().apply {
                    put("dni", dni)
                    put("nombre", nombre)
                    put("apellido", apellido)
                    put("telefono", telefono)
                    put("aptoFisico", 1)
                    put("fechaAlta", fechaAlta)
                }

                db.insertOrThrow("persona", null, datosPersona)

            } else {
                // Una persona existente debe tener el apto registrado
                if (!tieneApto && !aptoFisico) return -1L

                // Si presenta el apto ahora, actualizar su registro
                if (!tieneApto && aptoFisico) {
                    val datosApto = ContentValues().apply {
                        put("aptoFisico", 1)
                    }

                    db.update(
                        "persona",
                        datosApto,
                        "dni = ?",
                        arrayOf(dni)
                    )
                }
            }

            // Impedir que una persona sea registrada dos veces como socio
            val cursorSocio = db.rawQuery(
                "SELECT idSocio FROM socio WHERE persona_dni = ?",
                arrayOf(dni)
            )

            val yaEsSocio = cursorSocio.use {
                it.moveToFirst()
            }

            if (yaEsSocio) return -1L

            // Obtener el valor vigente de la cuota mensual
            val importe = obtenerValorCuotaMensual(fechaAlta)
                ?: return -1L

            // Registrar al socio
            val datosSocio = ContentValues().apply {
                put("persona_dni", dni)
                put("fechaAlta", fechaAlta)
                put("estadoActivo", 1)
                put("carnetEntregado", 0)
            }

            val idSocio = db.insertOrThrow(
                "socio",
                null,
                datosSocio
            )

            // Calcular el vencimiento de la primera cuota
            val calendario = java.util.Calendar.getInstance()
            calendario.time = formato.parse(fechaAlta)!!
            calendario.add(java.util.Calendar.MONTH, 1)

            val fechaVencimiento = formato.format(calendario.time)

            // Generar la primera cuota pendiente de pago
            val datosCuota = ContentValues().apply {
                put("idSocio", idSocio)
                put("fechaInicio", fechaAlta)
                put("fechaVencimiento", fechaVencimiento)
                put("monto", importe)
                put("pagada", 0)
            }

            db.insertOrThrow("cuota", null, datosCuota)

            // Confirmar las operaciones
            db.setTransactionSuccessful()
            return idSocio

        } catch (e: Exception) {
            return -1L
        } finally {
            if (db.inTransaction()) {
                db.endTransaction()
            }
        }
    }



    /** Inserta un nuevo Socio en la Base de Datos (Versión anterior) **/
    fun insertarSocio(
        nombre: String,
        apellido: String,
        dni: String,
        alias: String,
        telefono: String,
        aptoFisico: Int,
        actividad: String,
        fechaAlta: String,
        estado: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("alias", alias)
            put("telefono", telefono)
            put("apto_fisico", aptoFisico)
            put("actividad", actividad)
            put("fecha_alta", fechaAlta)
            put("estado", estado)
        }
        return db.insert("socios", null, values)
    }

    /** Inserta un nuevo No Socio (visitante) */
    fun insertarNoSocio(
        nombre: String,
        apellido: String,
        dni: String,
        telefono: String,
        actividad: String,
        horario: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("telefono", telefono)
            put("actividad", actividad)
            put("horario", horario)
        }
        return db.insert("no_socios", null, values)
    }

    // =========================================================================
    // MÉTODOS DE BÚSQUEDA Y CONSULTA
    // =========================================================================

    /** Verifica si una persona ya está registrada */
    fun existePersona(dni: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT dni FROM persona WHERE dni = ?",
            arrayOf(dni)
        )

        val existe = cursor.moveToFirst()
        cursor.close()

        return existe
    }


    /** Obtiene las actividades disponibles con sus importes */
    fun obtenerActividades(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT idActividad, nombre, importe FROM actividad ORDER BY nombre",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    mapOf(
                        "idActividad" to it.getInt(0).toString(),
                        "nombre" to it.getString(1),
                        "importe" to it.getDouble(2).toString()
                    )
                )
            }
        }

        return lista
    }


    /** Obtiene los horarios correspondientes a una actividad */
    fun obtenerHorariosPorActividad(idActividad: Int): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT idHorario, diaSemana, horaInicio, horaFin
            FROM horarioactividad
            WHERE idActividad = ?
            ORDER BY idHorario
            """.trimIndent(),
            arrayOf(idActividad.toString())
        )

        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    mapOf(
                        "idHorario" to it.getInt(0).toString(),
                        "diaSemana" to it.getString(1),
                        "horaInicio" to it.getString(2),
                        "horaFin" to it.getString(3)
                    )
                )
            }
        }

        return lista
    }


    /** Obtiene el importe de la cuota mensual vigente */
    fun obtenerValorCuotaMensual(fecha: String): Double? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT importe
            FROM valor_cuota_mensual
            WHERE fechaVigencia <= ?
            ORDER BY fechaVigencia DESC
            LIMIT 1
            """.trimIndent(),
            arrayOf(fecha)
        )

        val importe = cursor.use {
            if (it.moveToFirst()) it.getDouble(0) else null
        }

        return importe
    }


    /** Busca una persona por DNI para la pantalla de pagos */
    fun buscarPersonaPorDni(dni: String): Map<String, String>? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                persona.nombre,
                persona.apellido,
                socio.idSocio
            FROM persona
            LEFT JOIN socio
                ON persona.dni = socio.persona_dni
            WHERE persona.dni = ?
            """.trimIndent(),
            arrayOf(dni)
        )

        return cursor.use {
            if (it.moveToFirst()) {
                val esSocio = !it.isNull(2)

                mapOf(
                    "nombre" to it.getString(0),
                    "apellido" to it.getString(1),
                    "tipoPersona" to if (esSocio) "Socio" else "No Socio",
                    "estado" to if (esSocio) "Socio registrado" else "Visitante"
                )
            } else {
                null
            }
        }
    }


    /** Obtiene los datos de un socio para mostrar su carnet */
    fun obtenerSocioPorDni(dni: String): Map<String, String>? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT
                socio.idSocio,
                persona.nombre,
                persona.apellido,
                persona.dni,
                socio.fechaAlta,
                CASE
                    WHEN EXISTS (
                        SELECT 1 FROM cuota
                        WHERE cuota.idSocio = socio.idSocio
                        AND cuota.pagada = 0
                        AND cuota.fechaVencimiento < date('now', 'localtime')
                    ) THEN 'Vencido'
                    WHEN EXISTS (
                        SELECT 1 FROM cuota
                        WHERE cuota.idSocio = socio.idSocio
                        AND cuota.pagada = 0
                    ) THEN 'Pendiente de pago'
                    ELSE 'Al día'
                END AS estado
            FROM socio
            INNER JOIN persona
                ON socio.persona_dni = persona.dni
            WHERE persona.dni = ?
            """.trimIndent(),
            arrayOf(dni)
        )

        return cursor.use {
            if (it.moveToFirst()) {
                mapOf(
                    "id" to it.getInt(0).toString(),
                    "nombre" to it.getString(1),
                    "apellido" to it.getString(2),
                    "dni" to it.getString(3),
                    "fechaAlta" to it.getString(4),
                    "estado" to it.getString(5)
                )
            } else {
                null
            }
        }
    }


    // =========================================================================
    // MÉTODOS DE PAGO Y COBRO
    // =========================================================================


    /** Obtiene la primera cuota pendiente de un socio */
    fun obtenerCuotaPendiente(dni: String): Map<String, String>? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT cuota.idCuota, cuota.monto, cuota.fechaVencimiento
            FROM cuota
            INNER JOIN socio ON cuota.idSocio = socio.idSocio
            WHERE socio.persona_dni = ?
              AND cuota.pagada = 0
            ORDER BY cuota.fechaVencimiento ASC
            LIMIT 1
            """.trimIndent(),
            arrayOf(dni)
        )

        return cursor.use {
            if (it.moveToFirst()) {
                mapOf(
                    "idCuota" to it.getInt(0).toString(),
                    "monto" to it.getDouble(1).toString(),
                    "fechaVencimiento" to it.getString(2)
                )
            } else {
                null
            }
        }
    }


    /** Obtiene la primera visita pendiente de pago de una persona */
    fun obtenerVisitaPendiente(dni: String): Map<String, String>? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT idVisita, importe, fechaAsistencia
            FROM visita
            WHERE persona_dni = ?
              AND pagada = 0
            ORDER BY idVisita ASC
            LIMIT 1
            """.trimIndent(),
            arrayOf(dni)
        )

        return cursor.use {
            if (it.moveToFirst()) {
                mapOf(
                    "idVisita" to it.getInt(0).toString(),
                    "monto" to it.getDouble(1).toString(),
                    "fechaAsistencia" to it.getString(2)
                )
            } else {
                null
            }
        }
    }

    /** Registra el pago de una cuota o de una visita */
    fun registrarPago(
        idCuota: Int?,
        idVisita: Int?,
        medioPago: String,
        cuotasTarjeta: Int?
    ): Long {
        // El pago debe corresponder a una cuota o a una visita, no a ambas
        if ((idCuota == null) == (idVisita == null)) return -1L

        val db = writableDatabase

        try {
            db.beginTransaction()

            val esCuota = idCuota != null
            val tabla = if (esCuota) "cuota" else "visita"
            val columnaId = if (esCuota) "idCuota" else "idVisita"
            val id = idCuota ?: idVisita!!


            // Obtener el importe real de la cuota o visita pendiente
            val columnaImporte = if (esCuota) "monto" else "importe"

            val cursor = db.rawQuery(
                "SELECT $columnaImporte FROM $tabla WHERE $columnaId = ? AND pagada = 0",
                arrayOf(id.toString())
            )

            val importe = cursor.use {
                if (it.moveToFirst()) it.getDouble(0) else return -1L
            }


            val fechaPago = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.US
            ).format(java.util.Date())

            // Guardar el pago
            val datosPago = ContentValues().apply {
                if (esCuota) {
                    put("idCuota", id)
                } else {
                    put("idVisita", id)
                }

                put("fechaPago", fechaPago)
                put("monto", importe)
                put("medioPago", medioPago)

                if (cuotasTarjeta != null) {
                    put("cuotasTarjeta", cuotasTarjeta)
                }
            }

            val idPago = db.insertOrThrow("pago", null, datosPago)

            // Marcar la cuota o visita como pagada
            val datosEstado = ContentValues().apply {
                put("pagada", 1)
            }

            db.update(
                tabla,
                datosEstado,
                "$columnaId = ?",
                arrayOf(id.toString())
            )

            db.setTransactionSuccessful()
            return idPago

        } catch (e: android.database.SQLException) {
            return -1L
        } finally {
            if (db.inTransaction()) {
                db.endTransaction()
            }
        }
    }



    /** Registra un pago y actualiza el estado del socio si corresponde (Versión anterior) */
    fun insertarPago(
        dniPersona: String,
        nombrePersona: String,
        tipoPersona: String,
        concepto: String,
        medioPago: String,
        cuotas: String,
        monto: Double,
        fechaPago: String,
        fechaVencimiento: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("dni_persona", dniPersona)
            put("nombre_persona", nombrePersona)
            put("tipo_persona", tipoPersona)
            put("concepto", concepto)
            put("medio_pago", medioPago)
            put("cuotas", cuotas)
            put("monto", monto)
            put("fecha_pago", fechaPago)
            put("fecha_vencimiento", fechaVencimiento)
        }
        val idPago = db.insert("pagos", null, values)

        // Si es socio y pagó la cuota, actualiza su estado a "Al Día"
        if (idPago != -1L && tipoPersona == "Socio") {
            val updateValues = ContentValues().apply {
                put("estado", "Al Día")
            }
            db.update("socios", updateValues, "dni = ?", arrayOf(dniPersona))
        }

        return idPago
    }

    // =========================================================================
    // CONSULTA DE VENCIMIENTOS CON ORDENAMIENTO (ORDER BY)
    // =========================================================================


    /** Consulta los vencimientos de los socios desde SQLite */
    fun obtenerVencimientos(filtroEstado: String): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT persona.nombre, persona.apellido, persona.dni,
                CASE
                    WHEN EXISTS (
                        SELECT 1 FROM cuota
                        WHERE cuota.idSocio = socio.idSocio
                        AND pagada = 0
                        AND fechaVencimiento < date('now', 'localtime')
                    ) THEN 'Deudor'
                    WHEN EXISTS (
                        SELECT 1 FROM cuota
                        WHERE cuota.idSocio = socio.idSocio
                        AND pagada = 0
                        AND fechaVencimiento = date('now', 'localtime')
                    ) THEN 'Vence Hoy'
                    WHEN EXISTS (
                        SELECT 1 FROM cuota
                        WHERE cuota.idSocio = socio.idSocio
                        AND pagada = 0
                    ) THEN 'Pendiente de Pago'
                    ELSE 'Al Día'
                END AS estado,
                COALESCE((
                    SELECT monto FROM cuota
                    WHERE cuota.idSocio = socio.idSocio
                    AND pagada = 0
                    ORDER BY fechaVencimiento ASC
                    LIMIT 1
                
                ), 0) AS monto,
                (
                    SELECT fechaVencimiento
                    FROM cuota
                    WHERE cuota.idSocio = socio.idSocio
                    AND pagada = 0
                    ORDER BY fechaVencimiento ASC
                    LIMIT 1
                ) AS fechaVencimiento

            FROM socio
            INNER JOIN persona ON socio.persona_dni = persona.dni
            ORDER BY persona.apellido ASC
            """.trimIndent(),
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val estado = it.getString(3)

                if (filtroEstado.equals("Todos", true) ||
                    filtroEstado == estado) {
                    lista.add(
                        mapOf(
                            "nombre" to it.getString(0),
                            "apellido" to it.getString(1),
                            "dni" to it.getString(2),
                            "estado" to estado,
                            "monto" to it.getDouble(4).toString(),
                            "fechaVencimiento" to (it.getString(5) ?: "")
                        )
                    )
                }
            }
        }

        return lista
    }


    companion object {
        private const val DATABASE_NAME = "ClubDeportivo.db"
        private const val DATABASE_VERSION = 2

        // Tabla USUARIOS: credenciales de acceso al sistema.
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE NOT NULL,
                contrasena TEXT NOT NULL
            )
        """

        // Tabla SOCIOS (anterior): registro de socios de la primera versión.
        private const val CREATE_TABLE_SOCIOS = """
            CREATE TABLE socios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT UNIQUE NOT NULL,
                alias TEXT UNIQUE NOT NULL,
                telefono TEXT,
                apto_fisico INTEGER NOT NULL,
                actividad TEXT,
                fecha_alta TEXT NOT NULL,
                estado TEXT NOT NULL
            )
        """
        // Tabla NO_SOCIOS (anterior): registro de visitantes de la primera versión.
        private const val CREATE_TABLE_NO_SOCIOS = """
            CREATE TABLE no_socios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                dni TEXT UNIQUE NOT NULL,
                telefono TEXT,
                actividad TEXT,
                horario TEXT
            )
        """

        // Tabla PAGOS (anterior): registro de pagos de la primera versión.
        private const val CREATE_TABLE_PAGOS = """
            CREATE TABLE pagos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dni_persona TEXT NOT NULL,
                nombre_persona TEXT NOT NULL,
                tipo_persona TEXT NOT NULL,
                concepto TEXT NOT NULL,
                medio_pago TEXT NOT NULL,
                cuotas TEXT,
                monto REAL NOT NULL,
                fecha_pago TEXT NOT NULL,
                fecha_vencimiento TEXT NOT NULL
            )
        """

        // Tabla PERSONA: datos personales comunes de socios y no socios.
        // El vencimiento del apto se deja sin utilizar por ahora.

        private val CREATE_TABLE_PERSONA = """
            CREATE TABLE persona (
                dni TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                telefono TEXT,
                aptoFisico INTEGER NOT NULL DEFAULT 0
                    CHECK (aptoFisico IN (0,1)),
                fechaAlta TEXT NOT NULL,
                fechaVencimientoApto TEXT
            )
        """


        // Tabla SOCIO: información específica del socio y estado del carnet.
        private const val CREATE_TABLE_SOCIO = """
            CREATE TABLE socio (
                idSocio INTEGER PRIMARY KEY AUTOINCREMENT,
                persona_dni TEXT NOT NULL UNIQUE,
                fechaAlta TEXT NOT NULL,
                estadoActivo INTEGER NOT NULL DEFAULT 1
                    CHECK (estadoActivo IN (0, 1)),
                carnetEntregado INTEGER NOT NULL DEFAULT 0
                    CHECK (carnetEntregado IN (0, 1)),
                fechaEntregaCarnet TEXT,
                FOREIGN KEY (persona_dni) REFERENCES persona(dni)
            )
        """

        // Tabla CUOTA: cuotas mensuales, importes y vencimientos de los socios.
        private const val CREATE_TABLE_CUOTA = """
            CREATE TABLE cuota (
                idCuota INTEGER PRIMARY KEY AUTOINCREMENT,
                idSocio INTEGER NOT NULL,
                fechaInicio TEXT NOT NULL,
                fechaVencimiento TEXT NOT NULL,
                monto REAL NOT NULL CHECK (monto > 0),
                pagada INTEGER NOT NULL DEFAULT 0
                    CHECK (pagada IN (0, 1)),
                FOREIGN KEY (idSocio) REFERENCES socio(idSocio)
            )
        """

        // Tabla ACTIVIDAD: actividades disponibles e importe por visita.
        private const val CREATE_TABLE_ACTIVIDAD = """
            CREATE TABLE actividad (
                idActividad INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                profesor TEXT,
                importe REAL NOT NULL CHECK (importe > 0)
            )
        """


        // Tabla HORARIOACTIVIDAD: días y horarios de cada actividad.
        private const val CREATE_TABLE_HORARIO_ACTIVIDAD = """
            CREATE TABLE horarioactividad (
                idHorario INTEGER PRIMARY KEY AUTOINCREMENT,
                idActividad INTEGER NOT NULL,
                diaSemana TEXT NOT NULL,
                horaInicio TEXT NOT NULL,
                horaFin TEXT NOT NULL,
                FOREIGN KEY (idActividad) REFERENCES actividad(idActividad)
            )
        """


        // Tabla VISITA: registro de cada asistencia de un no socio.
        private const val CREATE_TABLE_VISITA = """
            CREATE TABLE visita (
                idVisita INTEGER PRIMARY KEY AUTOINCREMENT,
                persona_dni TEXT NOT NULL,
                idHorario INTEGER NOT NULL,
                fechaAsistencia TEXT NOT NULL,
                fechaRegistro TEXT NOT NULL,
                importe REAL NOT NULL CHECK (importe > 0),
                pagada INTEGER NOT NULL DEFAULT 0
                    CHECK (pagada IN (0, 1)),
                FOREIGN KEY (persona_dni) REFERENCES persona(dni),
                FOREIGN KEY (idHorario) REFERENCES horarioactividad(idHorario)
            )
        """


        // Tabla PAGO: cobros de cuotas mensuales y visitas.
        private const val CREATE_TABLE_PAGO = """
            CREATE TABLE pago (
                idPago INTEGER PRIMARY KEY AUTOINCREMENT,
                idCuota INTEGER UNIQUE,
                idVisita INTEGER UNIQUE,
                fechaPago TEXT NOT NULL,
                monto REAL NOT NULL CHECK (monto > 0),
                medioPago TEXT NOT NULL,
                cuotasTarjeta INTEGER,
                FOREIGN KEY (idCuota) REFERENCES cuota(idCuota),
                FOREIGN KEY (idVisita) REFERENCES visita(idVisita),
                CHECK (
                    (idCuota IS NOT NULL AND idVisita IS NULL)
                    OR
                    (idCuota IS NULL AND idVisita IS NOT NULL)
                ),
                CHECK (
                    (medioPago = 'Efectivo' AND cuotasTarjeta IS NULL)
                    OR
                    (medioPago = 'Tarjeta de crédito'
                        AND cuotasTarjeta IN (1, 3, 6))
                )
            )
        """


        // Tabla VALOR_CUOTA_MENSUAL: historial de importes de la cuota mensual.
        private const val CREATE_TABLE_VALOR_CUOTA_MENSUAL = """
            CREATE TABLE valor_cuota_mensual (
                idValor INTEGER PRIMARY KEY AUTOINCREMENT,
                importe REAL NOT NULL CHECK (importe > 0),
                fechaVigencia TEXT NOT NULL UNIQUE
            )
        """








    }
}