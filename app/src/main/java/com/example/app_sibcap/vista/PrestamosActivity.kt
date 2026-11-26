package com.example.app_sibcap.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_sibcap.R
import com.example.app_sibcap.adaptador.PrestamoAdapter
import com.example.app_sibcap.modelo.Prestamo
import com.example.app_sibcap.modelo.ResumenPrestamos
import com.example.app_sibcap.modelo.SesionManager
import com.example.app_sibcap.presentador.FiltroPrestamos
import com.example.app_sibcap.presentador.MisPrestamosContract
import com.example.app_sibcap.presentador.MisPrestamosPresenter
import com.example.app_sibcap.utils.Formato

class PrestamosActivity : AppCompatActivity(), MisPrestamosContract.View {

    private lateinit var presentador: MisPrestamosContract.Presenter
    private lateinit var estadoView: EstadoView
    private lateinit var tvBienvenida: TextView
    private lateinit var tvLibrosPrestados: TextView
    private lateinit var tvLibrosVencidos: TextView
    private lateinit var tvMultaTotal: TextView
    private lateinit var tvTarifa: TextView
    private lateinit var spFiltro: Spinner
    private lateinit var tvSinResultados: TextView
    private lateinit var rvPrestamos: RecyclerView
    private lateinit var adapter: PrestamoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_prestamos)

        estadoView = findViewById(R.id.estadoView)
        tvBienvenida = findViewById(R.id.tvBienvenida)
        tvLibrosPrestados = findViewById(R.id.tvLibrosPrestados)
        tvLibrosVencidos = findViewById(R.id.tvLibrosVencidos)
        tvMultaTotal = findViewById(R.id.tvMultaTotal)
        tvTarifa = findViewById(R.id.tvTarifa)
        spFiltro = findViewById(R.id.spFiltro)
        tvSinResultados = findViewById(R.id.tvSinResultados)
        rvPrestamos = findViewById(R.id.rvPrestamos)
        findViewById<View>(R.id.btnVolver).setOnClickListener { finish() }

        tvBienvenida.text = "Hola, ${SesionManager(this).nombre() ?: "lector"}"

        adapter = PrestamoAdapter(emptyList()) { prestamo ->
            val intent = Intent(this, DetallePrestamoActivity::class.java)
            intent.putExtra("id_prestamo", prestamo.idPrestamo)
            startActivity(intent)
        }
        rvPrestamos.layoutManager = LinearLayoutManager(this)
        rvPrestamos.adapter = adapter

        configurarFiltro()

        presentador = MisPrestamosPresenter(this, SesionManager(this))
        presentador.cargarPrestamos()
    }

    private fun configurarFiltro() {
        val opciones = listOf("Todos", "Vigentes", "Vencidos")
        spFiltro.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val filtro = when (position) {
                    1 -> FiltroPrestamos.VIGENTES
                    2 -> FiltroPrestamos.VENCIDOS
                    else -> FiltroPrestamos.TODOS
                }
                presentador.filtrar(filtro)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroy() {
        if (::presentador.isInitialized) presentador.destruir()
        super.onDestroy()
    }

    override fun mostrarCarga() {
        estadoView.mostrarCarga("Cargando préstamos", "Estamos consultando tus libros prestados.")
    }

    override fun ocultarCarga() {
        estadoView.ocultarEstado()
    }

    override fun mostrarResumen(resumen: ResumenPrestamos, librosVencidos: Int) {
        tvLibrosPrestados.text = resumen.prestamosActivos.toString()
        tvLibrosVencidos.text = librosVencidos.toString()
        tvMultaTotal.text = Formato.mxn(resumen.totalMultaPendiente)
        tvTarifa.text = "Tarifa: ${Formato.mxn(resumen.multaPorDia)} por día de retraso"
    }

    override fun mostrarPrestamos(prestamos: List<Prestamo>) {
        tvSinResultados.visibility = View.GONE
        rvPrestamos.visibility = View.VISIBLE
        adapter.actualizarLista(prestamos)
    }

    override fun mostrarVacio(mensaje: String, sinPrestamos: Boolean) {
        rvPrestamos.visibility = View.GONE
        tvSinResultados.visibility = View.VISIBLE
        tvSinResultados.text = mensaje
    }

    override fun mostrarError(mensaje: String, sinConexion: Boolean) {
        val titulo = if (sinConexion) "No pudimos conectar" else "No pudimos cargar tus préstamos"
        estadoView.mostrarError(titulo, mensaje) { presentador.reintentar() }
    }

    override fun sesionExpirada() {
        Toast.makeText(this, "Tu sesión expiró. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}