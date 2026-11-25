package com.example.app_sibcap.vista

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_sibcap.R
import com.example.app_sibcap.presentador.clsPerfilPresenter
import com.example.app_sibcap.modelo.DatosPerfil
import android.widget.Toast

class PerfilActivity : AppCompatActivity() {

    private lateinit var datoNombre: View
    private lateinit var datoApellidoPaterno: View
    private lateinit var datoApellidoMaterno: View
    private lateinit var datoCorreo: View
    private lateinit var datoUsuario: View
    private lateinit var datoTelefono: View

    // PRESENTADOR
    private lateinit var presenter: clsPerfilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        inicializarComponentes()
        presenter = clsPerfilPresenter(this)

        val token = intent.getStringExtra("TOKEN")

        if (!token.isNullOrBlank()) {
            presenter.obtenerPerfil(token)
        } else {
            mostrarError("Debes iniciar sesión para consultar tu perfil.")
        }

    }


    private fun inicializarComponentes() {

        datoNombre = findViewById(R.id.datoNombre)
        datoApellidoPaterno = findViewById(R.id.datoApellidoPaterno)
        datoApellidoMaterno = findViewById(R.id.datoApellidoMaterno)
        datoCorreo = findViewById(R.id.datoCorreo)
        datoUsuario = findViewById(R.id.datoUsuario)
        datoTelefono = findViewById(R.id.datoTelefono)
    }


    private fun mostrarDatosEjemplo() {

        configurarDato(datoNombre, "Nombre(s)", "Valeria")
        configurarDato(datoApellidoPaterno, "Apellido paterno", "López")
        configurarDato(datoApellidoMaterno, "Apellido materno", "García")
        configurarDato(datoCorreo, "Correo electrónico", "valeria@ejemplo.mx")
        configurarDato(datoUsuario, "Usuario", "valeria.lopez")
        configurarDato(datoTelefono, "Teléfono", "771 000 0000")
    }

    private fun configurarDato(
        contenedor: View,
        etiqueta: String,
        valor: String
    ) {
        val txtEtiqueta = contenedor.findViewById<TextView>(R.id.txtEtiquetaDato)
        val txtValor = contenedor.findViewById<TextView>(R.id.txtValorDato)

        txtEtiqueta.text = etiqueta
        txtValor.text = valor
    }


    fun mostrarPerfil(datos: DatosPerfil) {
        configurarDato(datoNombre, "Nombres", datos.nombres)
        configurarDato(datoApellidoPaterno, "Apellido paterno", datos.apellidoPaterno)
        configurarDato(datoApellidoMaterno, "Apellido materno", datos.apellidoMaterno ?: "")
        configurarDato(datoCorreo, "Correo", datos.correo)
        configurarDato(datoUsuario, "Nombre de usuario", datos.nombreUsuario)
        configurarDato(datoTelefono, "Teléfono", datos.telefono ?: "")
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

}