package com.example.app_sibcap.vista

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_sibcap.R
import android.content.Intent
import com.example.app_sibcap.presentador.clsOpcionesCuentaPresenter

class OpcionesCuentaActivity : AppCompatActivity() {

    private lateinit var btnEditarInformacion: Button
    private lateinit var btnCambiarContrasenia: Button
    private lateinit var btnAccesibilidad: Button
    private lateinit var btnCerrarSesion: Button

    // PRESENTADOR
    private lateinit var presenter: clsOpcionesCuentaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_opciones_cuenta)

        Plantilla.aplicarMargenes(this)
        Plantilla.configurarRegreso(this, "Opciones de cuenta")

        inicializarComponentes()
        presenter = clsOpcionesCuentaPresenter(this)
        configurarEventos()
    }

    private fun inicializarComponentes() {
        btnEditarInformacion = findViewById(R.id.btnEditarInformacion)
        btnCambiarContrasenia = findViewById(R.id.btnCambiarContrasenia)
        btnAccesibilidad = findViewById(R.id.btnAccesibilidad)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
    }

    private fun configurarEventos() {

        btnEditarInformacion.setOnClickListener {
            presenter.editarInformacion()
        }

        btnCambiarContrasenia.setOnClickListener {
            presenter.cambiarContrasenia()
        }

        btnAccesibilidad.setOnClickListener {
            presenter.abrirAccesibilidad()
        }

        btnCerrarSesion.setOnClickListener {
            presenter.cerrarSesion()
        }
    }

    fun abrirEditarInformacion() {
        val intent = Intent(this, EditarPerfilActivity::class.java)
        startActivity(intent)
    }
}