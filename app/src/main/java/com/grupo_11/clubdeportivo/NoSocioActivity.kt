package com.grupo_11.clubdeportivo

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoSocioActivity : AppCompatActivity() {

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_no_socio)

        // Instanciamos el Helper de SQLite
        dbHelper = AdminSQLiteOpenHelper(this)

        // BARRA SUPERIOR
        val navbarNoSocio = findViewById<MaterialToolbar>(R.id.navbarNoSocio)
        navbarNoSocio.setNavigationOnClickListener { finish() }

        // CONTROLES DEL FORMULARIO
        val edtNombre = findViewById<TextInputEditText>(R.id.edtNombre)
        val edtApellido = findViewById<TextInputEditText>(R.id.edtApellido)
        val edtDni = findViewById<TextInputEditText>(R.id.edtDni)
        val edtTelefono = findViewById<TextInputEditText>(R.id.edtTelefono)
        val chkAptoFisico = findViewById<MaterialCheckBox>(R.id.chkAptoFisico)
        val dropdownActividad = findViewById<AutoCompleteTextView>(R.id.dropdownActividad)
        val dropdownHorario = findViewById<AutoCompleteTextView>(R.id.dropdownHorario)
        val edtMonto = findViewById<TextInputEditText>(R.id.edtMonto)
        val edtFecha = findViewById<TextInputEditText>(R.id.edtFecha)
        val btnLimpiar = findViewById<MaterialButton>(R.id.btnLimpiar)

        val formatoVisible = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoSQLite = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val fechaSeleccionada = Calendar.getInstance()
        edtFecha.setText(formatoVisible.format(fechaSeleccionada.time))
        edtFecha.setOnClickListener {
            DatePickerDialog(
                this,
                { _, anio, mes, dia ->
                    fechaSeleccionada.set(anio, mes, dia)
                    edtFecha.setText(formatoVisible.format(fechaSeleccionada.time))
                },
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH),
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // DESPLEGABLES
        // Obtener las actividades desde SQLite
        val actividades = dbHelper.obtenerActividades()
        val nombresActividades = actividades.map { it["nombre"] ?: "" }

        dropdownActividad.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresActividades)
        )
        // Fuerza la apertura al tocar: con inputType="none" algunos estilos no despliegan la lista.
        dropdownActividad.setOnClickListener { dropdownActividad.showDropDown() }
        dropdownActividad.setOnFocusChangeListener { _, tieneFoco ->
            if (tieneFoco) dropdownActividad.showDropDown()
        }
        dropdownHorario.setOnClickListener { dropdownHorario.showDropDown() }
        dropdownHorario.setOnFocusChangeListener { _, tieneFoco ->
            if (tieneFoco) dropdownHorario.showDropDown()
        }

        if (actividades.isEmpty()) {
            Toast.makeText(this, "No hay actividades cargadas en SQLite", Toast.LENGTH_LONG).show()
        }

        // Mostrar los horarios de la actividad seleccionada
        var idHorarioSeleccionado: Int? = null
        dropdownActividad.setOnItemClickListener { _, _, position, _ ->
            val idActividad = actividades[position]["idActividad"]?.toIntOrNull()

            dropdownHorario.setText("", false)
            idHorarioSeleccionado = null
            edtMonto.setText("")
            // El importe se obtiene del registro de actividad, no de strings.xml.
            edtMonto.setText(actividades.getOrNull(position)?.get("importe")?.toDoubleOrNull()?.let {
                String.format(Locale("es", "AR"), "$ %,.0f", it)
            } ?: "")

            if (idActividad != null) {
                val horarios = dbHelper.obtenerHorariosPorActividad(idActividad)
                val nombresHorarios = horarios.map {
                    "${it["diaSemana"]} de ${it["horaInicio"]} a ${it["horaFin"]}"
                }
                dropdownHorario.setAdapter(
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresHorarios)
                )
            }
        }

        dropdownHorario.setOnItemClickListener { _, _, position, _ ->
            val idActividad = actividades.find {
                it["nombre"] == dropdownActividad.text.toString()
            }?.get("idActividad")?.toIntOrNull()

            if (idActividad != null) {
                val horarios = dbHelper.obtenerHorariosPorActividad(idActividad)
                idHorarioSeleccionado =
                    horarios.getOrNull(position)?.get("idHorario")?.toIntOrNull()
            }
        }

        btnLimpiar.setOnClickListener {
            edtNombre.text?.clear()
            edtApellido.text?.clear()
            edtDni.text?.clear()
            edtTelefono.text?.clear()
            chkAptoFisico.isChecked = false
            dropdownActividad.setText("", false)
            dropdownHorario.setText("", false)
            dropdownHorario.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, emptyList<String>()))
            idHorarioSeleccionado = null
            edtMonto.setText("")
            fechaSeleccionada.timeInMillis = System.currentTimeMillis()
            edtFecha.setText(formatoVisible.format(fechaSeleccionada.time))
        }

        // BOTÓN GUARDAR (INSERCIÓN EN SQLITE)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarNoSocio)
        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString().trim()
            val apellido = edtApellido.text.toString().trim()
            val dni = edtDni.text.toString().trim()
            val telefono = edtTelefono.text.toString().trim()
            val actividad = dropdownActividad.text.toString().trim()
            val horario = dropdownHorario.text.toString().trim()

            // VALIDACIONES DEL FORMULARIO
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
            if (telefono.isNotEmpty() &&
                (telefono.length !in 8..15 || !telefono.all { it.isDigit() })
            ) {
                edtTelefono.error = "Ingrese un teléfono de 8 a 15 números"
                edtTelefono.requestFocus()
                return@setOnClickListener
            }

            // 5. Validar apto físico
            if (!chkAptoFisico.isChecked) {
                Toast.makeText(this, "Es obligatorio presentar el apto físico", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 6. Validar actividad seleccionada desde SQLite
            if (actividad.isEmpty() || actividad !in nombresActividades) {
                dropdownActividad.error = "Seleccione una actividad válida"
                dropdownActividad.requestFocus()
                return@setOnClickListener
            }

            // 7. Validar horario seleccionado desde SQLite
            val idHorario = idHorarioSeleccionado
            val idActividad = actividades.find { it["nombre"] == actividad }
                ?.get("idActividad")?.toIntOrNull()
            val horariosValidos = if (idActividad != null) {
                dbHelper.obtenerHorariosPorActividad(idActividad)
            } else {
                emptyList()
            }
            val horarioValido = horariosValidos.any {
                it["idHorario"]?.toIntOrNull() == idHorario &&
                    "${it["diaSemana"]} de ${it["horaInicio"]} a ${it["horaFin"]}" == horario
            }
            if (horario.isEmpty() || idHorario == null || !horarioValido) {
                dropdownHorario.error = "Seleccione un horario válido"
                dropdownHorario.requestFocus()
                return@setOnClickListener
            }

            // El DNI puede existir: una persona puede registrar varias visitas.
            // Registrar la visita en las nuevas tablas SQLite
            val idReg = dbHelper.registrarVisita(
                dni = dni,
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                aptoFisico = chkAptoFisico.isChecked,
                idHorario = idHorario,
                fechaAsistencia = formatoSQLite.format(fechaSeleccionada.time)
            )

            if (idReg != -1L) {
                Toast.makeText(this, "Pase diario registrado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NoSocioRegistradoActivity::class.java).apply {
                    putExtra("nombre", "$nombre $apellido")
                    putExtra("dni", dni)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar pase diario", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
