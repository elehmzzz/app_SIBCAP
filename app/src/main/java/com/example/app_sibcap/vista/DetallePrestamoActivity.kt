package com.example.app_sibcap.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_sibcap.R
import com.example.app_sibcap.modelo.PrestamoDetalle
import com.example.app_sibcap.modelo.SesionManager
import com.example.app_sibcap.presentador.DetallePrestamoContract
import com.example.app_sibcap.presentador.DetallePrestamoPresenter
import com.example.app_sibcap.utils.Formato

// Una sola pantalla para préstamo con multa y sin multa (el bloque de multa es condicional)
class DetallePrestamoActivity : AppCompatActivity(), DetallePrestamoContract.View {

    private lateinit var presentador: DetallePrestamoContract.Presenter
    private lateinit var estadoView: EstadoView
    private lateinit var tvSubtitulo: TextView
    private lateinit var tvFechaPrestamo: TextView
    private lateinit var tvFechaLimite: TextView
    private lateinit var tarjetaEstado: View
    private lateinit var tvEstado: TextView
    private lateinit var tvDiasEtiqueta: TextView
    private lateinit var tvDias: TextView
    private lateinit var tarjetaMulta: View
    private lateinit var tvMultaEtiqueta: TextView
    private lateinit var tvMultaMonto: TextView
    private lateinit var tvCodigo: TextView
    private lateinit var tvLibro: TextView
    private lateinit var tvAutor: TextView
    private lateinit var tvIsbn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_prestamo)

        estadoView = findViewById(R.id.estadoView)
        tvSubtitulo = findViewById(R.id.tvSubtitulo)
        tvFechaPrestamo = findViewById(R.id.tvFechaPrestamo)
        tvFechaLimite = findViewById(R.id.tvFechaLimite)
        tarjetaEstado = findViewById(R.id.tarjetaEstado)
        tvEstado = findViewById(R.id.tvEstado)
        tvDiasEtiqueta = findViewById(R.id.tvDiasEtiqueta)
        tvDias = findViewById(R.id.tvDias)
        tarjetaMulta = findViewById(R.id.tarjetaMulta)
        tvMultaEtiqueta = findViewById(R.id.tvMultaEtiqueta)
        tvMultaMonto = findViewById(R.id.tvMultaMonto)
        tvCodigo = findViewById(R.id.tvCodigo)
        tvLibro = findViewById(R.id.tvLibro)
        tvAutor = findViewById(R.id.tvAutor)
        tvIsbn = findViewById(R.id.tvIsbn)
        findViewById<ImageButton>(R.id.btnVolver).setOnClickListener { finish() }

        val idPrestamo = intent.getIntExtra("id_prestamo", -1)
        if (idPrestamo <= 0) {
            Toast.makeText(this, "Préstamo inválido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        presentador = DetallePrestamoPresenter(this, SesionManager(this))
        presentador.cargarDetalle(idPrestamo)
    }

    override fun onDestroy() {
        if (::presentador.isInitialized) presentador.destruir()
        super.onDestroy()
    }

    // ---------- DetallePrestamoContract.View ----------
    override fun mostrarCarga() {
        estadoView.mostrarCarga("Cargando préstamo", "Estamos consultando los datos. Espera un momento.")
    }

    override fun ocultarCarga() {
        estadoView.ocultarEstado()
    }

    override fun mostrarError(mensaje: String, sinConexion: Boolean) {
        val titulo = if (sinConexion) "No pudimos conectar" else "No pudimos cargar el préstamo"
        estadoView.mostrarError(titulo, mensaje) { presentador.reintentar() }
    }

    override fun mostrarDetalle(detalle: PrestamoDetalle) {
        estadoView.ocultarEstado()

        tvFechaPrestamo.text = Formato.fecha(detalle.fechaPrestamo)
        tvFechaLimite.text = Formato.fecha(detalle.fechaDevolucion)

        // Estado: Vigente / Vencido como en el diseño; Devuelto y Multa pagada usan el nombre del servidor
        val activoATiempo = detalle.idEstatus == 1 && !detalle.vencido
        val esAviso = detalle.vencido || detalle.idEstatus == 3
        tvEstado.text = when {
            activoATiempo -> "Vigente"
            esAviso -> "Vencido"
            else -> detalle.estatus
        }
        tarjetaEstado.setBackgroundResource(if (esAviso) R.drawable.bg_chip_vencido else R.drawable.bg_chip_vigente)

        // Segunda tarjeta: días restantes (a tiempo) o días de retraso (todo lo demás). Valores del servidor.
        if (activoATiempo) {
            tvDiasEtiqueta.text = "Días restantes"
            tvDias.text = (detalle.diasRestantes ?: 0).toString()
            tvDias.setTextColor(ContextCompat.getColor(this, R.color.sib_primario))
        } else {
            tvDiasEtiqueta.text = "Días de retraso"
            tvDias.text = detalle.diasRetraso.toString()
            tvDias.setTextColor(
                ContextCompat.getColor(this, if (detalle.diasRetraso > 0) R.color.sib_multa else R.color.sib_primario)
            )
        }

        // Bloque de multa: SOLO si el servidor devuelve una multa
        if (detalle.multa > 0) {
            tarjetaMulta.visibility = View.VISIBLE
            tvMultaEtiqueta.text = if (detalle.multaPagada) "Multa pagada" else "Multa acumulada"
            tvMultaMonto.text = Formato.mxn(detalle.multa)
            tvSubtitulo.text = "Consulta la información y el adeudo de este libro."
        } else {
            tarjetaMulta.visibility = View.GONE
            tvSubtitulo.text = "Consulta la información y la fecha de devolución de este libro."
        }

        tvCodigo.text = Formato.codigoPrestamo(detalle.idPrestamo)
        tvLibro.text = detalle.titulo
        tvAutor.text = detalle.autor
        tvIsbn.text = detalle.isbn
    }

    override fun sesionExpirada() {
        Toast.makeText(this, "Tu sesión expiró. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
