package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class CarnetActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        // =========================
        // BARRA SUPERIOR
        // =========================
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // =========================
        // LECTURA DESDE SQLITE
        // =========================
        val txtNumeroSocio = findViewById<TextView>(R.id.txtNumeroSocio)
        val txtNombreSocio = findViewById<TextView>(R.id.txtNombreSocio)
        val txtDniSocio = findViewById<TextView>(R.id.txtDniSocio)
        val txtEstadoSocio = findViewById<TextView>(R.id.txtEstadoSocio)
        val txtFechaAlta = findViewById<TextView>(R.id.txtFechaAlta)

        // Recuperamos el DNI enviado por Intent (o usamos un valor por defecto)
        val dniRecibido = intent.getStringExtra("dni") ?: ""
        val socio = dbHelper.obtenerSocioPorDni(dniRecibido)

        if (socio != null) {
            val idSocio = socio["id"] ?: "1"
            txtNumeroSocio.text = "#${idSocio.padStart(4, '0')}"
            txtNombreSocio.text = "${socio["nombre"]} ${socio["apellido"]}"
            txtDniSocio.text = "DNI ${socio["dni"]}"
            txtEstadoSocio.text = socio["estado"]
            val fechaAlta = socio["fechaAlta"] ?: ""
            val formatoBD = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formatoPantalla = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            txtFechaAlta.text = formatoBD.parse(fechaAlta)?.let {
		    formatoPantalla.format(it)
		} ?: fechaAlta
        } else {
            // Si viene de demostración sin DNI específico, muestra datos de respaldo
            txtNumeroSocio.text = "#0001"
            txtNombreSocio.text = "Socio Registrado"
            txtDniSocio.text = "DNI $dniRecibido"
            txtEstadoSocio.text = "Pendiente de Pago"
            txtFechaAlta.text = "Fecha actual"
        }

        // =========================
        // BOTÓN IR AL INICIO
        // =========================
        val btnIrInicio = findViewById<MaterialButton>(R.id.btnIrInicio)
        btnIrInicio.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // Ajuste de barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}