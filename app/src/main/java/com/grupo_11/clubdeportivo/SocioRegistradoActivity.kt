package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class SocioRegistradoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_socio_registrado)

        // BARRA SUPERIOR
        val navbarSocioRegistrado = findViewById<MaterialToolbar>(R.id.navbarSocioRegistrado)
        navbarSocioRegistrado.setNavigationOnClickListener { finish() }

        // 1. Recuperamos el DNI enviado desde SocioActivity
        val dniSocio = intent.getStringExtra("dni") ?: ""

        // 2. BOTÓN VER CARNET: Reenvía el DNI a CarnetActivity para consultar SQLite
        val btnVerCarnet = findViewById<MaterialButton>(R.id.btnVerCarnet)
        btnVerCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java).apply {
                putExtra("dni", dniSocio)
            }
            startActivity(intent)
        }

        // 3. BOTÓN IR AL INICIO
        val btnIrAlInicio = findViewById<MaterialButton>(R.id.btnIrAlInicio)
        btnIrAlInicio.setOnClickListener {
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