package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la interfaz utilice el área completa de la pantalla
        enableEdgeToEdge()

        // Asocia la Activity con su archivo de diseño XML
        setContentView(R.layout.activity_principal)

        // =========================
        // AJUSTE DE BARRAS DEL SISTEMA
        // =========================

        // Obtiene el espacio ocupado por las barras del sistema
        // y lo aplica como margen interno de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio de las barras del sistema como padding
            // para evitar que el contenido quede oculto detrás de ellas
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // =========================
        // TARJETAS DEL MENÚ
        // =========================

        // Configura la tarjeta para registrar un nuevo socio
        configurarCard(
            findViewById(R.id.cardNuevoSocio),
            R.drawable.ic_person_add,
            R.string.nuevo_socio,
            R.string.nuevo_socio_desc,
            R.color.secondary
        )

        // Configura la tarjeta para gestionar visitantes
        configurarCard(
            findViewById(R.id.cardVisitantes),
            R.drawable.ic_person,
            R.string.visitantes,
            R.string.visitantes_desc,
            R.color.accent
        )

        // Configura la tarjeta para gestionar pagos
        configurarCard(
            findViewById(R.id.cardPagos),
            R.drawable.ic_payment_filled,
            R.string.pagos,
            R.string.pagos_desc,
            R.color.warning
        )

        // Configura la tarjeta para consultar vencimientos
        configurarCard(
            findViewById(R.id.cardVencimientos),
            R.drawable.ic_calendar,
            R.string.vencimientos,
            R.string.vencimientos_desc,
            R.color.danger
        )

        // =========================
        // NAVEGACIÓN - NUEVO SOCIO
        // =========================

        // Obtiene la referencia a la tarjeta Nuevo Socio
        val cardNuevoSocio = findViewById<View>(R.id.cardNuevoSocio)

        // Al presionar la tarjeta, abre la pantalla de registro de socios
        cardNuevoSocio.setOnClickListener {
            val intent = Intent(this, SocioActivity::class.java)
            startActivity(intent)
        }

        // =========================
        // NAVEGACIÓN - REGISTRAR VISITA
        // =========================

        // Obtiene la referencia a la tarjeta Visitantes
        val cardVisitantes = findViewById<View>(R.id.cardVisitantes)

        // Al presionar la tarjeta, abre la pantalla de registro de visitas
        cardVisitantes.setOnClickListener {
            val intent = Intent(this, NoSocioActivity::class.java)
            startActivity(intent)
        }
        // ============================
        // NAVEGACIÓN - REGISTRAR PAGOS
        // ============================

        // Abre la pantalla para registrar pagos
        val cardPagos = findViewById<View>(R.id.cardPagos)

        cardPagos.setOnClickListener {
            val intent = Intent(this, PagoActivity::class.java)
            startActivity(intent)
        }

        // =========================
        // NAVEGACIÓN - VENCIMIENTOS
        // =========================

        // Obtiene la referencia a la tarjeta Vencimientos
        val cardVencimientos = findViewById<View>(R.id.cardVencimientos)

        // Al presionar la tarjeta, abre la pantalla de vencimientos
        cardVencimientos.setOnClickListener {
            val intent = Intent(this, VencimientosActivity::class.java)
            startActivity(intent)
        }

        // =========================
        // CERRAR SESIÓN
        // =========================

        // Obtiene la referencia al botón de cerrar sesión
        val btnCerrarSesion = findViewById<ImageButton>(R.id.btnCerrarSesion)

        // Regresa a la pantalla de inicio de sesión y cierra la pantalla principal
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // =========================
        // NAVEGACIÓN INFERIOR
        // =========================

        // Configura las acciones de los ítems del menú inferior
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_inicio -> true

                R.id.nav_socios -> {
                    val intent = Intent(this, SocioActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_pagos -> {
                    val intent = Intent(this, PagoActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    // Configura el contenido visual de una tarjeta del menú principal
    private fun configurarCard(
        card: View,
        icono: Int,
        titulo: Int,
        descripcion: Int,
        color: Int
    ) {

        // Obtiene los elementos internos de la tarjeta
        val imgIcon = card.findViewById<ImageView>(R.id.imgCardIcon)
        val txtTitle = card.findViewById<TextView>(R.id.txtCardTitle)
        val txtDescription = card.findViewById<TextView>(R.id.txtCardDescription)

        // Asigna el ícono y aplica el color correspondiente
        imgIcon.setImageResource(icono)
        imgIcon.setColorFilter(ContextCompat.getColor(this, color))

        // Asigna el título y la descripción de la tarjeta
        txtTitle.setText(titulo)
        txtDescription.setText(descripcion)
    }
}