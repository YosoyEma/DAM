package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class NoSocioRegistradoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_no_socio_registrado)

        // =========================
        // NAVEGACIÓN - VOLVER
        // =========================

        val navbarNoSocioRegistrado =
            findViewById<MaterialToolbar>(R.id.navbarNoSocioRegistrado)

        navbarNoSocioRegistrado.setNavigationOnClickListener {
            finish()
        }

        // =========================
        // BOTÓN IR AL INICIO
        // =========================

        val btnIrAlInicio =
            findViewById<MaterialButton>(R.id.btnIrAlInicio)

        btnIrAlInicio.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
        
        // =========================
        // AJUSTE DE BARRAS DEL SISTEMA
        // =========================

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }
}