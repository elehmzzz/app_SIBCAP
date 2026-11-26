package com.example.app_sibcap.vista

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.app_sibcap.R

// Componente compartido (MOCKUP ESTADOS del Figma): Cargando / Vacío / Error con botón de acción.
// Se coloca ENCIMA del contenido dentro de un FrameLayout. No hace llamadas de red:
// el botón solo avisa a la pantalla, que se lo pasa al Presenter.
class EstadoView @JvmOverloads constructor(
    context: Context,
    atributos: AttributeSet? = null
) : FrameLayout(context, atributos) {

    private val circulo: View
    private val progreso: ProgressBar
    private val simbolo: TextView
    private val tvTitulo: TextView
    private val tvMensaje: TextView
    private val boton: Button

    init {
        LayoutInflater.from(context).inflate(R.layout.view_estado, this, true)
        circulo = findViewById(R.id.estadoCirculo)
        progreso = findViewById(R.id.estadoProgreso)
        simbolo = findViewById(R.id.estadoSimbolo)
        tvTitulo = findViewById(R.id.estadoTitulo)
        tvMensaje = findViewById(R.id.estadoMensaje)
        boton = findViewById(R.id.estadoBoton)

        setBackgroundColor(ContextCompat.getColor(context, R.color.sib_fondo))
        isClickable = true                      // evita toques en el contenido mientras se muestra
        visibility = View.GONE
    }

    // Círculo amarillo con indicador de progreso
    fun mostrarCarga(titulo: String = "Cargando", mensaje: String = "Espera un momento.") {
        colorCirculo(R.color.sib_circulo_carga)
        progreso.visibility = View.VISIBLE
        simbolo.visibility = View.GONE
        poner(titulo, mensaje)
        boton.visibility = View.GONE
    }

    // Círculo claro con el símbolo ▤. El botón es opcional (ej. "Ir al catálogo").
    fun mostrarVacio(
        titulo: String,
        mensaje: String,
        textoBoton: String? = null,
        alPulsar: (() -> Unit)? = null
    ) {
        colorCirculo(R.color.sib_tarjeta)
        progreso.visibility = View.GONE
        simbolo.visibility = View.VISIBLE
        simbolo.text = "▤"
        simbolo.setTextColor(ContextCompat.getColor(context, R.color.sib_primario))
        poner(titulo, mensaje)
        configurarBoton(textoBoton, alPulsar)
    }

    // Círculo rosado con "!" y botón Reintentar
    fun mostrarError(titulo: String, mensaje: String, alReintentar: () -> Unit) {
        colorCirculo(R.color.sib_circulo_error)
        progreso.visibility = View.GONE
        simbolo.visibility = View.VISIBLE
        simbolo.text = "!"
        simbolo.setTextColor(ContextCompat.getColor(context, R.color.sib_error_icono))
        poner(titulo, mensaje)
        configurarBoton("Reintentar", alReintentar)
    }

    fun ocultarEstado() {
        visibility = View.GONE
    }

    // Un solo drawable (bg_circulo_estado) para los 3 estados: solo cambia el color
    private fun colorCirculo(color: Int) {
        circulo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

    private fun poner(titulo: String, mensaje: String) {
        visibility = View.VISIBLE
        tvTitulo.text = titulo
        tvMensaje.text = mensaje
    }

    private fun configurarBoton(texto: String?, accion: (() -> Unit)?) {
        if (texto == null || accion == null) {
            boton.visibility = View.GONE
            return
        }
        boton.text = texto
        boton.visibility = View.VISIBLE
        boton.setOnClickListener { accion() }
    }
}
