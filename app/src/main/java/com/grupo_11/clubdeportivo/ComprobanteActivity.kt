package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComprobanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprobante)

        // =========================
        // BARRA SUPERIOR
        // =========================
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // =========================
        // DATOS DEL COMPROBANTE REAL
        // =========================
        val nombre = intent.getStringExtra("nombre") ?: ""
        val dni = intent.getStringExtra("dni") ?: ""
        val medioPago = intent.getStringExtra("medioPago") ?: ""
        val cuotas = intent.getStringExtra("cuotas") ?: "-"
        val monto = intent.getStringExtra("monto") ?: ""

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        findViewById<TextView>(R.id.txtFecha).text = fechaActual
        findViewById<TextView>(R.id.txtNombre).text = nombre
        findViewById<TextView>(R.id.txtDni).text = dni
        findViewById<TextView>(R.id.txtConcepto).text = intent.getStringExtra("concepto") ?: ""
        findViewById<TextView>(R.id.txtMedioPago).text = medioPago
        findViewById<TextView>(R.id.txtCuotas).text = cuotas
        findViewById<TextView>(R.id.txtMonto).text = monto
        findViewById<TextView>(R.id.txtEstado).text = "PAGADO (Registrado en BD)"

        // =========================
        // IMPRIMIR COMPROBANTE
        // =========================
        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)
        btnImprimir.setOnClickListener {
            Toast.makeText(
                this,
                "Comprobante $dni impreso con éxito",
                Toast.LENGTH_SHORT
            ).show()
        }

        // =========================
        // IR AL INICIO
        // =========================
        val btnInicio = findViewById<MaterialButton>(R.id.btnInicio)
        btnInicio.setOnClickListener {
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