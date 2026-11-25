package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.clsLibroModelo
import com.example.app_sibcap.modelo.dataCategorias
import com.example.app_sibcap.modelo.dataLibros
import com.example.app_sibcap.vista.CatalogoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * Catálogo — Responsable: Jhonatan
 * Modelo: clsLibroModelo   Servicio: catalogo.php
 */
class clsCatalogoPresenter(val vista: CatalogoActivity) {

    private val modelo = clsLibroModelo()
    private var listaCategorias: List<dataCategorias> = emptyList()
    private var librosEncontrados: List<dataLibros> = emptyList()

    private var busquedaActual = ""
    private var categoriaActual = 0 // 0 = todas
    private var cuantosMostrar = LIBROS_POR_PAGINA

    fun iniciar(nombre: String?) {
        if (!nombre.isNullOrBlank()) {
            vista.mostrarBienvenida("¡Bienvenido(a), $nombre!")
        }

        // Se usa un Scope simple para no bloquear la interfaz
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 1. Cargar la lista de categorías en el Spinner
                listaCategorias = modelo.obtenerCategorias()
                vista.mostrarCategorias(listaCategorias)
            } catch (e: Exception) {
                vista.mostrarError("No se pudieron cargar las categorías: " + e.localizedMessage)
            }
        }

        // 2. Cargar TODOS los libros por defecto
        cargarLibros()
    }

    fun buscar(texto: String) {
        busquedaActual = texto.trim()
        cargarLibros()
    }

    fun seleccionarCategoria(posicion: Int) {
        if (posicion >= 0 && posicion < listaCategorias.size) {
            val categoria = listaCategorias[posicion]
            // El Spinner avisa al cargarse (posición 0): si no cambió, no se vuelve a pedir
            if (categoria.id != categoriaActual) {
                categoriaActual = categoria.id
                cargarLibros()
            }
        }
    }

    private fun cargarLibros() {
        CoroutineScope(Dispatchers.Main).launch {
            vista.mostrarCargando(true)
            try {
                // Aquí recibes el RETURN directo del modelo
                librosEncontrados = modelo.obtenerCatalogo(busquedaActual, categoriaActual)
                vista.mostrarCargando(false)
                cuantosMostrar = LIBROS_POR_PAGINA
                mostrarLibrosVisibles()
            } catch (e: Exception) {
                vista.mostrarCargando(false)
                librosEncontrados = emptyList()
                vista.mostrarMensaje("No se pudo cargar el catálogo. Revisa tu conexión.")
                vista.mostrarBotonVerMas(false)
                vista.mostrarError("Error al cargar libros: " + e.localizedMessage)
            }
        }
    }

    // Botón "Ver más libros"
    fun verMasLibros() {
        cuantosMostrar += LIBROS_POR_PAGINA
        mostrarLibrosVisibles()
    }

    private fun mostrarLibrosVisibles() {
        if (librosEncontrados.isEmpty()) {
            if (busquedaActual.isEmpty() && categoriaActual == 0) {
                vista.mostrarMensaje("Aún no hay libros en el catálogo.")
            } else {
                vista.mostrarMensaje("No se encontraron libros con esa búsqueda.")
            }
            vista.mostrarBotonVerMas(false)
            return
        }
        vista.mostrarLibros(librosEncontrados.take(cuantosMostrar))
        vista.mostrarBotonVerMas(cuantosMostrar < librosEncontrados.size)
    }

    // Lo llama la vista cuando tocan "Ver detalles" en una tarjeta
    fun seleccionarLibro(libro: dataLibros) {
        vista.abrirDetalleLibro(libro.idLibro, libro.titulo, libro.autor)
    }

    companion object {
        private const val LIBROS_POR_PAGINA = 5
    }
}
