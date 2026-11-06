package com.grupo_11.clubdeportivo

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class VencimientosActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper
    private lateinit var contenedorVencimientos: LinearLayout
    private lateinit var txtVencenHoy: TextView
    private lateinit var txtDeudores: TextView
    private lateinit var txtTotalSocios: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vencimientos)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        // =========================
        // BARRA SUPERIOR
        // =========================
        val navbarVencimientos = findViewById<MaterialToolbar>(R.id.navbarVencimientos)
        navbarVencimientos.setNavigationOnClickListener { finish() }

        // =========================
        // CONTROLES DE LA PANTALLA
        // =========================
        contenedorVencimientos = findViewById(R.id.contenedorVencimientos)
        txtVencenHoy = findViewById(R.id.txtVencenHoy)
        txtDeudores = findViewById(R.id.txtDeudores)
        txtTotalSocios = findViewById(R.id.txtTotalSocios)

        val radioGroupFiltros = findViewById<RadioGroup>(R.id.radioGroupFiltros)
        radioGroupFiltros.check(R.id.radioVencenHoy)

        // Actualizamos las 3 tarjetas superiores de contadores desde SQLite
        actualizarIndicadores()

        // Carga inicial: socios cuyas cuotas vencen hoy
        cargarVencimientosDesdeBD("Vence Hoy")

        // =============================
        // EVENTO RADIO BUTTONS (FILTROS)
        // =============================
        radioGroupFiltros.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioDeudores -> cargarVencimientosDesdeBD("Deudor")
                R.id.radioVencenHoy -> cargarVencimientosDesdeBD("Vence Hoy")
                else -> cargarVencimientosDesdeBD("Todos")
            }
        }

        // Ajuste de barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Calcula los contadores de la cabecera leyendo desde SQLite
     */
    private fun actualizarIndicadores() {
        val todosSocios = dbHelper.obtenerVencimientos("Todos")
        val deudoresCount = todosSocios.count {
            it["estado"] == "Deudor"
        }
        val vencenHoyCount = todosSocios.count { it["estado"] == "Vence Hoy" }

        txtTotalSocios.text = todosSocios.size.toString()
        txtDeudores.text = deudoresCount.toString()
        txtVencenHoy.text = vencenHoyCount.toString()
    }

    /**
     * Carga dinámicamente las filas en el contenedor usando component_vencimientos.xml
     */
    private fun cargarVencimientosDesdeBD(filtroEstado: String) {
        // Limpiamos las vistas anteriores
        contenedorVencimientos.removeAllViews()

        // Obtenemos los socios filtrados y ordenados desde SQLite
        val listaSocios = dbHelper.obtenerVencimientos(filtroEstado)

        for (socio in listaSocios) {
            // Inflamos la vista reutilizable component_vencimientos.xml
            val vistaFila = layoutInflater.inflate(
                R.layout.component_vencimientos,
                contenedorVencimientos,
                false
            )

            val txtNombreSocio = vistaFila.findViewById<TextView>(R.id.txtNombreSocio)
            val txtMontoVencimiento = vistaFila.findViewById<TextView>(R.id.txtMontoVencimiento)
            val txtDetalleVencimiento = vistaFila.findViewById<TextView>(R.id.txtDetalleVencimiento)

            val nombreCompleto = "${socio["apellido"]}, ${socio["nombre"]}"
            val dni = socio["dni"] ?: ""
            val estado = socio["estado"] ?: ""

            txtNombreSocio.text = nombreCompleto
            txtMontoVencimiento.text = "$${socio["monto"]}"
            val fecha = socio["fechaVencimiento"] ?: ""
            val vencimiento = if (fecha.isNotEmpty()) " · Vence: ${fecha.split("-").reversed().joinToString("/")}" else ""
            txtDetalleVencimiento.text = "DNI: $dni · $estado$vencimiento"

            // Agregamos la fila inflada al contenedor de la pantalla
            contenedorVencimientos.addView(vistaFila)
        }
    }
}