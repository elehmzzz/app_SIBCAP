package com.example.app_sibcap.vista

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.app_sibcap.R
import com.example.app_sibcap.modelo.dataDetalleLibro
import com.example.app_sibcap.presentador.clsDetalleLibroPresenter

/*
 * Detalle del libro — Responsable: Jhonatan
 */
class DetalleLibroActivity : AppCompatActivity() {

    private lateinit var imgPortada: ImageView
    private lateinit var txtTitulo: TextView
    private lateinit var txtAutor: TextView
    private lateinit var txtCategoria: TextView
    private lateinit var txtDisponibles: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var txtIsbn: TextView
    private lateinit var pbDetalle: ProgressBar

    //creamos nuestro objeto presentador
    private lateinit var presentador: clsDetalleLibroPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_libro)
        // Plantilla compartida: encabezado con flecha + barra inferior
        Plantilla.aplicarMargenes(this)
        Plantilla.configurarRegreso(this, "Detalles del libro")
        Plantilla.configurarNavegacion(this, Plantilla.CATALOGO)

        // 1. Enlazar componentes XML
        imgPortada = findViewById(R.id.imgDetallePortada)
        txtTitulo = findViewById(R.id.txtDetalleTitulo)
        txtAutor = findViewById(R.id.txtDetalleAutor)
        txtCategoria = findViewById(R.id.txtDetalleCategoria)
        txtDisponibles = findViewById(R.id.txtDetalleDisponibles)
        txtDescripcion = findViewById(R.id.txtDetalleDescripcion)
        txtIsbn = findViewById(R.id.txtDetalleIsbn)
        pbDetalle = findViewById(R.id.pbDetalle)

        // 2. Obtener los datos del libro enviados desde CatalogoActivity
        val id = intent.getIntExtra("LIBRO_ID", 0)
        val titulo = intent.getStringExtra("TITULO_LIBRO") ?: ""
        val autor = intent.getStringExtra("AUTOR_LIBRO") ?: ""

        // Se los ponemos a los TextView mientras carga lo demás
        txtTitulo.text = titulo
        txtAutor.text = autor

        // Inicializar presentador y cargar el detalle
        presentador = clsDetalleLibroPresenter(this)
        if (id != 0) {
            presentador.cargarDetalle(id)
        } else {
            mostrarError("No se indicó qué libro mostrar.")
        }
    }//cierra el oncreate

    //Salidas
    fun mostrarCargando(cargando: Boolean) {
        pbDetalle.visibility = if (cargando) View.VISIBLE else View.GONE
    }

    fun mostrarDetalle(libro: dataDetalleLibro, isbn: String) {
        txtTitulo.text = libro.titulo
        txtAutor.text = libro.autor
        txtCategoria.text = libro.categoria
        txtDescripcion.text = libro.sinopsis ?: "Sin descripción."
        txtIsbn.text = isbn

        // Disponibles en verde; si no hay ejemplares, el 0 en rojo
        txtDisponibles.text = libro.ejemplaresDisponibles.toString()
        val color = if (libro.disponible) R.color.verde_principal else R.color.rojo_no_disponible
        txtDisponibles.setTextColor(ContextCompat.getColor(this, color))

        Glide.with(this)
            .load(libro.portadaUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .into(imgPortada)
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
