package com.example.app_sibcap.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_sibcap.R
import com.example.app_sibcap.adaptador.LibroAdapter
import com.example.app_sibcap.modelo.dataCategorias
import com.example.app_sibcap.modelo.dataLibros
import com.example.app_sibcap.presentador.clsCatalogoPresenter

/*
 * Catálogo — Responsable: Jhonatan
 */
class CatalogoActivity : AppCompatActivity() {

    private lateinit var txtBienvenida: TextView
    private lateinit var txtBuscar: EditText
    private lateinit var btnBuscar: ImageButton
    private lateinit var spinnerCategorias: Spinner
    private lateinit var pbCatalogo: ProgressBar
    private lateinit var txtMensaje: TextView
    private lateinit var recyclerLibros: RecyclerView
    private lateinit var btnVerMas: Button
    private lateinit var adapterLibros: LibroAdapter

    //creamos nuestro objeto presentador
    private lateinit var presentador: clsCatalogoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo)
        // Plantilla compartida: encabezado verde + barra inferior
        Plantilla.aplicarMargenes(this)
        Plantilla.configurarNavegacion(this, Plantilla.CATALOGO)

        txtBienvenida = findViewById(R.id.txtBienvenida)
        txtBuscar = findViewById(R.id.txtBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)
        spinnerCategorias = findViewById(R.id.spnCategorias)
        pbCatalogo = findViewById(R.id.pbCatalogo)
        txtMensaje = findViewById(R.id.txtMensaje)
        recyclerLibros = findViewById(R.id.rvLibros)
        btnVerMas = findViewById(R.id.btnVerMas)

        recyclerLibros.layoutManager = LinearLayoutManager(this)
        adapterLibros = LibroAdapter(this, emptyList())
        recyclerLibros.adapter = adapterLibros

        //inicializar al presentador
        presentador = clsCatalogoPresenter(this)
        configurarListeners()
        presentador.iniciar(intent.getStringExtra("NOMBRE_USUARIO"))
    }//cierra el oncreate

    private fun configurarListeners() {
        btnBuscar.setOnClickListener(this::ejecutarBuscar)
        btnVerMas.setOnClickListener(this::ejecutarVerMas)

        // Buscar también con la tecla "buscar" del teclado
        txtBuscar.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) {
                ejecutarBuscar(txtBuscar)
                true
            } else {
                false
            }
        }

        spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presentador.seleccionarCategoria(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun ejecutarBuscar(v: View) {
        ocultarTeclado()
        presentador.buscar(txtBuscar.text.toString())
    }

    fun ejecutarVerMas(v: View) {
        presentador.verMasLibros()
    }

    //Salidas
    fun mostrarBienvenida(texto: String) {
        txtBienvenida.text = texto
        txtBienvenida.visibility = View.VISIBLE
    }

    fun mostrarCategorias(categorias: List<dataCategorias>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategorias.adapter = adapter
    }

    fun mostrarCargando(cargando: Boolean) {
        pbCatalogo.visibility = if (cargando) View.VISIBLE else View.GONE
        if (cargando) txtMensaje.visibility = View.GONE
    }

    fun mostrarLibros(libros: List<dataLibros>) {
        txtMensaje.visibility = View.GONE
        recyclerLibros.visibility = View.VISIBLE
        adapterLibros.actualizarLista(libros)
    }

    fun mostrarMensaje(mensaje: String) {
        adapterLibros.actualizarLista(emptyList())
        recyclerLibros.visibility = View.GONE
        txtMensaje.text = mensaje
        txtMensaje.visibility = View.VISIBLE
    }

    fun mostrarBotonVerMas(mostrar: Boolean) {
        btnVerMas.visibility = if (mostrar) View.VISIBLE else View.GONE
    }

    // Lo llama el adaptador al tocar "Ver detalles"
    fun seleccionarLibro(libro: dataLibros) {
        presentador.seleccionarLibro(libro)
    }

    fun abrirDetalleLibro(idLibro: Int, titulo: String, autor: String) {
        val intent = Intent(this, DetalleLibroActivity::class.java)
        intent.putExtra("LIBRO_ID", idLibro)
        intent.putExtra("TITULO_LIBRO", titulo)
        intent.putExtra("AUTOR_LIBRO", autor)
        startActivity(intent)
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun ocultarTeclado() {
        val teclado = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        teclado.hideSoftInputFromWindow(txtBuscar.windowToken, 0)
    }
}
