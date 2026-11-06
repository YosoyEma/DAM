package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        val txtUsuario = findViewById<TextInputEditText>(R.id.txtUsuario)
        val txtContrasena = findViewById<TextInputEditText>(R.id.txtContrasena)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuario = txtUsuario?.text.toString().trim()
            val contrasena = txtContrasena?.text.toString().trim()


            // =========================
            // VALIDACIONES DEL FORMULARIO
            // =========================

            // 1. Validar usuario
            if (usuario.isEmpty()) {
                txtUsuario.error = "Ingrese el usuario"
                txtUsuario.requestFocus()
                return@setOnClickListener
            }

            // 2. Validar contraseña
            if (contrasena.isEmpty()) {
                txtContrasena.error = "Ingrese la contraseña"
                txtContrasena.requestFocus()
                return@setOnClickListener
            }

            // 3. Validar credenciales contra la Base de Datos SQLite
            val esValido = dbHelper.validarUsuario(usuario, contrasena)

            if (esValido) {
                Toast.makeText(
                    this,
                    getString(R.string.login_bienvenido),
                    Toast.LENGTH_SHORT
                ).show()

                // Navegar a la pantalla principal
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
                finish() // Cierra el Login para no retornar al presionar la flecha Atrás
            } else {
                Toast.makeText(
                    this,
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

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