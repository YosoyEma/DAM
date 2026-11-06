package com.grupo_11.clubdeportivo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText

class PagoActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    // Variables para almacenar la persona encontrada
    private var dniEncontrado: String = ""
    private var nombreCompletoEncontrado: String = ""
    private var tipoPersonaEncontrado: String = ""

    private var idCuotaPendiente: Int? = null
    private var idVisitaPendiente: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        // =========================
        // BARRA SUPERIOR
        // =========================
        val navbarPago = findViewById<MaterialToolbar>(R.id.navbarPago)
        navbarPago.setNavigationOnClickListener { finish() }

        // =========================
        // CONTROLES DE LA PANTALLA
        // =========================
        val edtBuscar = findViewById<TextInputEditText>(R.id.edtBuscar)
        val btnBuscar = findViewById<MaterialButton>(R.id.btnBuscar)
        val contenedorPersona = findViewById<View>(R.id.contenedorPersona)
        val txtNombrePersona = findViewById<android.widget.TextView>(R.id.txtNombrePersona)
        val txtTipoPersona = findViewById<android.widget.TextView>(R.id.txtTipoPersona)
        val txtDetallePersona = findViewById<android.widget.TextView>(R.id.txtDetallePersona)
        val edtMonto = findViewById<TextInputEditText>(R.id.edtMonto)
        edtMonto.isFocusable = false
        val btnGuardarPago = findViewById<MaterialButton>(R.id.btnGuardarPago)
        val dropdownMedioPago = findViewById<AutoCompleteTextView>(R.id.dropdownMedioPago)
        val contenedorCuotas = findViewById<View>(R.id.contenedorCuotas)
        val toggleCuotas = findViewById<MaterialButtonToggleGroup>(R.id.toggleCuotas)

        // =========================
        // BÚSQUEDA REAL EN SQLITE
        // =========================
        btnBuscar.setOnClickListener {

            ocultarTeclado()
            idCuotaPendiente = null
            idVisitaPendiente = null
            btnGuardarPago.isEnabled = false
            val dni = edtBuscar.text.toString().trim()

            // Reiniciar los datos de la búsqueda anterior
            dniEncontrado = ""
            nombreCompletoEncontrado = ""
            tipoPersonaEncontrado = ""
            contenedorPersona.visibility = View.GONE
            btnGuardarPago.isEnabled = false

            // =========================
            // VALIDACIONES DE BÚSQUEDA
            // =========================

            // 1. Validar DNI obligatorio
            if (dni.isEmpty()) {
                edtBuscar.error = "Ingrese el DNI"
                edtBuscar.requestFocus()
                return@setOnClickListener
            }

            // 2. Validar formato del DNI
            if (dni.length !in 7..8 || !dni.all { it.isDigit() }) {
                edtBuscar.error = "El DNI debe contener 7 u 8 números"
                edtBuscar.requestFocus()
                return@setOnClickListener
            }


            // Consulta la persona en SQLite (socio o no socio)
            val persona = dbHelper.buscarPersonaPorDni(dni)

            if (persona != null) {
                edtBuscar.error = null

                // Almacenamos los datos en variables
                dniEncontrado = dni
                nombreCompletoEncontrado = "${persona["nombre"]} ${persona["apellido"]}"
                tipoPersonaEncontrado = persona["tipoPersona"] ?: "Socio"

                // Mostramos la información en la UI
                txtNombrePersona.text = nombreCompletoEncontrado
                txtTipoPersona.text = tipoPersonaEncontrado
                txtDetallePersona.text = "DNI: $dni · Estado: ${persona["estado"]}"

                contenedorPersona.visibility = View.VISIBLE


                // Consultar el importe pendiente según el tipo de persona
                val pendiente = if (tipoPersonaEncontrado == "Socio") {
                    dbHelper.obtenerCuotaPendiente(dni)
                } else {
                    dbHelper.obtenerVisitaPendiente(dni)
                }

                if (pendiente != null) {
                    edtMonto.setText(pendiente["monto"] ?: "")

                    idCuotaPendiente = if (tipoPersonaEncontrado == "Socio") {
                        pendiente["idCuota"]?.toIntOrNull()
                    } else {
                        null
                    }

                    idVisitaPendiente = if (tipoPersonaEncontrado == "No Socio") {
                        pendiente["idVisita"]?.toIntOrNull()
                    } else {
                        null
                    }
                } else {
                    edtMonto.setText("")
                    Toast.makeText(
                        this,
                        "La persona no tiene pagos pendientes",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                // Evalúa si habilita el botón de pago
                validarEstadoBotonGuardar(
                    dropdownMedioPago.text.toString(),
                    contenedorCuotas.visibility == View.VISIBLE,
                    toggleCuotas.checkedButtonId,
                    btnGuardarPago
                )
            } else {
                contenedorPersona.visibility = View.GONE
                edtMonto.setText("")
                btnGuardarPago.isEnabled = false
                Toast.makeText(
                    this,
                    getString(R.string.mensaje_persona_no_encontrada),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // =========================
        // DROPDOWN MEDIO DE PAGO
        // =========================
        val mediosPago = resources.getStringArray(R.array.medios_pago)
        val adapterMediosPago = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            mediosPago
        )
        dropdownMedioPago.setAdapter(adapterMediosPago)


        dropdownMedioPago.setOnItemClickListener { _, _, position, _ ->
            contenedorCuotas.visibility =
                if (position == 1) View.VISIBLE else View.GONE

            validarEstadoBotonGuardar(
                dropdownMedioPago.text.toString(),
                contenedorCuotas.visibility == View.VISIBLE,
                toggleCuotas.checkedButtonId,
                btnGuardarPago
            )
        }

        toggleCuotas.addOnButtonCheckedListener { _, _, _ ->
            validarEstadoBotonGuardar(
                dropdownMedioPago.text.toString(),
                contenedorCuotas.visibility == View.VISIBLE,
                toggleCuotas.checkedButtonId,
                btnGuardarPago
            )
        }

        // =========================
        // GUARDAR PAGO EN SQLITE
        // =========================
        btnGuardarPago.setOnClickListener {

            val medioPago = dropdownMedioPago.text.toString().trim()
            val montoString = edtMonto.text.toString().trim()

            // =========================
            // VALIDACIONES DEL FORMULARIO
            // =========================

            // 1. Validar persona seleccionada
            if (dniEncontrado.isEmpty() ||
                dniEncontrado != edtBuscar.text.toString().trim()
                ) {
                edtBuscar.error = "Busque una persona registrada"
                edtBuscar.requestFocus()
                return@setOnClickListener
            }

            // 2. Validar importe obligatorio
            if (montoString.isEmpty()) {
                edtMonto.error = "Ingrese el importe"
                edtMonto.requestFocus()
                return@setOnClickListener
            }

            // 3. Validar importe numérico y mayor que cero
            val montoDouble = montoString.toDoubleOrNull()

            if (montoDouble == null ||
                !montoDouble.isFinite() ||
                montoDouble <= 0) {
                edtMonto.error = "Ingrese un importe válido mayor que cero"
                edtMonto.requestFocus()
                return@setOnClickListener
            }

            // 4. Validar medio de pago
            if (medioPago.isEmpty() || medioPago !in mediosPago) {
                dropdownMedioPago.error = "Seleccione un medio de pago válido"
                dropdownMedioPago.requestFocus()
                return@setOnClickListener
            }

            // 5. Validar cuotas para tarjeta de crédito
            val esTarjeta = medioPago == mediosPago[1]

            if (esTarjeta &&
                toggleCuotas.checkedButtonId != R.id.btnCuota1 &&
                toggleCuotas.checkedButtonId != R.id.btnCuota3 &&
                toggleCuotas.checkedButtonId != R.id.btnCuota6) {

                Toast.makeText(
                    this,
                    "Seleccione 1, 3 o 6 cuotas",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // 6. Determinar cuotas según el medio de pago
            val cuotas = if (esTarjeta) {
                when (toggleCuotas.checkedButtonId) {
                    R.id.btnCuota1 -> "1"
                    R.id.btnCuota3 -> "3"
                    R.id.btnCuota6 -> "6"
                    else -> "-"
                }
            } else {
                "-"
            }

            // Verificar que exista una cuota o visita pendiente
            if (idCuotaPendiente == null && idVisitaPendiente == null) {
                Toast.makeText(
                    this,
                    "No hay pagos pendientes para registrar",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val cuotasTarjeta = if (esTarjeta) {
                cuotas.toIntOrNull()
            } else {
                null
            }

            // Registrar el pago en la nueva tabla SQLite
            val idReg = dbHelper.registrarPago(
                idCuota = idCuotaPendiente,
                idVisita = idVisitaPendiente,
                medioPago = medioPago,
                cuotasTarjeta = cuotasTarjeta
            )

            if (idReg != -1L) {
                Toast.makeText(this, "Pago registrado en la Base de Datos", Toast.LENGTH_SHORT).show()

                // Abrimos el Comprobante pasando los datos mediante Intent
                val intent = Intent(this, ComprobanteActivity::class.java).apply {
                    putExtra("nombre", nombreCompletoEncontrado)
                    putExtra("dni", dniEncontrado)
                    putExtra("concepto", if (tipoPersonaEncontrado == "Socio") "Cuota mensual" else "Pase diario")
                    putExtra("medioPago", medioPago)
                    putExtra("cuotas", cuotas)
                    putExtra("monto", "$" + String.format(java.util.Locale("es", "AR"), "%,.0f", montoString.toDouble()))
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el pago en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }

        // Ajuste de barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun ocultarTeclado() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun validarEstadoBotonGuardar(
        medioPago: String,
        mostrarCuotas: Boolean,
        cuotaSeleccionadaId: Int,
        btnGuardar: MaterialButton
    ) {
        btnGuardar.isEnabled =
            (idCuotaPendiente != null || idVisitaPendiente != null) &&
                    medioPago.isNotEmpty() &&
                    (!mostrarCuotas || cuotaSeleccionadaId != View.NO_ID)
    }
}