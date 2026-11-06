package com.grupo_11.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SocioActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_socio)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        // =========================
        // NAVEGACIÓN - VOLVER
        // =========================
        val navbarSocio = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.navbarSocio)
        navbarSocio.setNavigationOnClickListener { finish() }

        // =========================
        // CONTROLES DEL FORMULARIO
        // =========================
        val edtNombre = findViewById<TextInputEditText>(R.id.edtNombre)
        val edtApellido = findViewById<TextInputEditText>(R.id.edtApellido)
        val edtDni = findViewById<TextInputEditText>(R.id.edtDni)
        val edtTelefono = findViewById<TextInputEditText>(R.id.edtTelefono)
        val cbAptoFisico = findViewById<CheckBox>(R.id.chkAptoFisico)
        val dropdownActividad = findViewById<AutoCompleteTextView>(R.id.dropdownActividad)

        // Carga del listado desplegable de actividades
        // Obtener las actividades desde SQLite
        val actividades = dbHelper.obtenerActividades()

        // Obtener los nombres de las actividades
        val nombresActividades = actividades.map { it["nombre"] ?: "" }

        // Mostrar las actividades en el desplegable
        val adapterActividades = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombresActividades
        )

        dropdownActividad.setAdapter(adapterActividades)

        // =========================
        // BOTÓN GUARDAR (ALTA BD)
        // =========================
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarSocio)

        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString().trim()
            val apellido = edtApellido.text.toString().trim()
            val dni = edtDni.text.toString().trim()
            val telefono = edtTelefono.text.toString().trim()
            val tieneAptoFisico = cbAptoFisico.isChecked

            val actividad = dropdownActividad.text.toString().trim()

            // =========================
            // VALIDACIONES DEL FORMULARIO
            // =========================

            // 1. Validar nombre
            if (nombre.isEmpty()) {
                edtNombre.error = "Ingrese el nombre"
                edtNombre.requestFocus()
                return@setOnClickListener
            }

            if (!nombre.all { it.isLetter() || it == ' ' }) {
                edtNombre.error = "El nombre solo puede contener letras"
                edtNombre.requestFocus()
                return@setOnClickListener
            }

            // 2. Validar apellido
            if (apellido.isEmpty()) {
                edtApellido.error = "Ingrese el apellido"
                edtApellido.requestFocus()
                return@setOnClickListener
            }

            if (!apellido.all { it.isLetter() || it == ' ' }) {
                edtApellido.error = "El apellido solo puede contener letras"
                edtApellido.requestFocus()
                return@setOnClickListener
            }

            // 3. Validar DNI
            if (dni.isEmpty()) {
                edtDni.error = "Ingrese el DNI"
                edtDni.requestFocus()
                return@setOnClickListener
            }

            if (dni.length !in 7..8 || !dni.all { it.isDigit() }) {
                edtDni.error = "El DNI debe contener 7 u 8 números"
                edtDni.requestFocus()
                return@setOnClickListener
            }

            // 4. Validar teléfono (opcional)
            if (telefono.isNotEmpty()) {
                if (telefono.length !in 8..15 || !telefono.all { it.isDigit() }) {
                    edtTelefono.error = "Ingrese un teléfono de 8 a 15 números"
                    edtTelefono.requestFocus()
                    return@setOnClickListener
                }
            }

            // 5. Validar apto físico
            if (!tieneAptoFisico) {
                Toast.makeText(
                    this,
                    "Es obligatorio presentar el apto físico",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // 6. Validar actividad (opcional)
            if (actividad.isNotEmpty() && actividad !in nombresActividades) {
                dropdownActividad.error = "Seleccione una actividad válida"
                dropdownActividad.requestFocus()
                return@setOnClickListener
            }

            // 7. Validar que el DNI no pertenezca ya a un socio
            if (dbHelper.obtenerSocioPorDni(dni) != null) {
                edtDni.error = "El DNI ya pertenece a un socio registrado"
                edtDni.requestFocus()
                return@setOnClickListener
            }

            // Registrar al socio en SQLite y generar su primera cuota
            val resultado = dbHelper.registrarSocio(
                dni = dni,
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                aptoFisico = tieneAptoFisico
            )

            if (resultado != -1L) {
                Toast.makeText(
                    this,
                    "Socio registrado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(
                    this,
                    SocioRegistradoActivity::class.java
                ).apply {
                    putExtra("nombre", "$nombre $apellido")
                    putExtra("dni", dni)
                    putExtra("idSocio", resultado.toString())
                }

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "No se pudo registrar al socio. Verifique si ya está registrado.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        // Ajuste de márgenes de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}