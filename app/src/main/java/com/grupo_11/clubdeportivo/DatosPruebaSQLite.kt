package com.grupo_11.clubdeportivo

import android.database.sqlite.SQLiteDatabase

object DatosPruebaSQLite {

    fun cargarDatosPrueba(db: SQLiteDatabase) {

        // Personas ficticias para probar el reporte de vencimientos
        db.execSQL("""
    INSERT OR IGNORE INTO persona
        (dni, nombre, apellido, aptoFisico, fechaAlta)
    VALUES
        ('30123456', 'Laura', 'Fernández', 1, date('now','localtime')),
        ('32456789', 'Martín', 'Gómez', 1, date('now','localtime')),
        ('35678912', 'Carolina', 'López', 1, date('now','localtime')),
        ('28765432', 'Diego', 'Martínez', 1, date('now','localtime')),
        ('33987654', 'Ana', 'Rodríguez', 1, date('now','localtime')),
        ('31234567', 'Pablo', 'Sánchez', 1, date('now','localtime')),
        ('36789123', 'Lucía', 'Torres', 1, date('now','localtime')),
        ('29876543', 'Marcos', 'Pérez', 1, date('now','localtime')),
        ('34567891', 'Sofía', 'Romero', 1, date('now','localtime')),
        ('37654321', 'Julián', 'Díaz', 1, date('now','localtime'))
""".trimIndent())

        // Registrar como socios a las 10 personas ficticias
        db.execSQL("""
    INSERT OR IGNORE INTO socio
        (idSocio, persona_dni, fechaAlta, estadoActivo, carnetEntregado)
    VALUES
        (901, '30123456', date('now','localtime'), 1, 0),
        (902, '32456789', date('now','localtime'), 1, 0),
        (903, '35678912', date('now','localtime'), 1, 0),
        (904, '28765432', date('now','localtime'), 1, 0),
        (905, '33987654', date('now','localtime'), 1, 0),
        (906, '31234567', date('now','localtime'), 1, 0),
        (907, '36789123', date('now','localtime'), 1, 0),
        (908, '29876543', date('now','localtime'), 1, 0),
        (909, '34567891', date('now','localtime'), 1, 0),
        (910, '37654321', date('now','localtime'), 1, 0)
""".trimIndent())

        // Cuotas ficticias para probar los filtros de Vencimientos
        db.execSQL("""
    INSERT OR IGNORE INTO cuota
        (idCuota, idSocio, fechaInicio, fechaVencimiento, monto, pagada)
    VALUES
        -- 4 socios deudores: cuotas vencidas e impagas
        (901, 901, date('now','localtime','-1 month'), date('now','localtime','-10 days'), 45000, 0),
        (902, 902, date('now','localtime','-1 month'), date('now','localtime','-7 days'), 45000, 0),
        (903, 903, date('now','localtime','-1 month'), date('now','localtime','-5 days'), 45000, 0),
        (904, 904, date('now','localtime','-1 month'), date('now','localtime','-2 days'), 45000, 0),

        -- 3 socios cuyas cuotas vencen hoy
        (905, 905, date('now','localtime','-1 month'), date('now','localtime'), 45000, 0),
        (906, 906, date('now','localtime','-1 month'), date('now','localtime'), 45000, 0),
        (907, 907, date('now','localtime','-1 month'), date('now','localtime'), 45000, 0),

        -- 3 socios con cuotas pagadas
        (908, 908, date('now','localtime','-1 month'), date('now','localtime','+10 days'), 45000, 1),
        (909, 909, date('now','localtime','-1 month'), date('now','localtime','+10 days'), 45000, 1),
        (910, 910, date('now','localtime','-1 month'), date('now','localtime','+10 days'), 45000, 1)
""".trimIndent())

        // Registra los pagos correspondientes a las cuotas ficticias pagadas.
        db.execSQL("""
    INSERT OR IGNORE INTO pago
        (idCuota, fechaPago, monto, medioPago)
    VALUES
        (908, date('now','localtime'), 45000, 'Efectivo'),
        (909, date('now','localtime'), 45000, 'Efectivo'),
        (910, date('now','localtime'), 45000, 'Efectivo')
""".trimIndent())

    }
}