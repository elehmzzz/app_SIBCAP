package com.example.app_sibcap.vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_sibcap.R
import android.widget.Toast
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.example.app_sibcap.presentador.clsEditarPerfilPresenter

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var edtUsuario: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var btnGuardarCambios: Button

    // PRESENTADOR
    private lateinit var presenter: clsEditarPerfilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)

        Plantilla.aplicarMargenes(this)
        Plantilla.configurarRegreso(this, "Editar información")

        inicializarComponentes()
        presenter = clsEditarPerfilPresenter(this)
        configurarEventos()
    }

    private fun inicializarComponentes() {
        edtUsuario = findViewById(R.id.edtUsuario)
        edtCorreo = findViewById(R.id.edtCorreo)
        edtTelefono = findViewById(R.id.edtTelefono)
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios)
    }

    private fun configurarEventos() {

        btnGuardarCambios.setOnClickListener {

            val usuario = edtUsuario.text.toString().trim()
            val correo = edtCorreo.text.toString().trim()
            val telefono = edtTelefono.text.toString().trim()

            presenter.guardarCambios(usuario, correo, telefono)
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    fun mostrarConfirmacion(
        usuario: String,
        correo: String,
        telefono: String
    ) {
        val dialogo = Dialog(this)

        dialogo.setContentView(R.layout.dialog_confirmar_edicion)

        val btnCancelar = dialogo.findViewById<Button>(
            R.id.btnCancelarEdicion
        )

        val btnConfirmar = dialogo.findViewById<Button>(
            R.id.btnConfirmarEdicion
        )

        btnCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        btnConfirmar.setOnClickListener {
            dialogo.dismiss()

            presenter.confirmarEdicion(usuario, correo, telefono)
        }

        dialogo.setCancelable(false)
        dialogo.show()

        dialogo.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            setLayout(
                (resources.displayMetrics.widthPixels * 0.88).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.30f)
        }
    }

}