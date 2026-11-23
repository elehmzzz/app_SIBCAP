package com.example.app_sibcap.modelo

/*
 * Modelo de libros — Responsable: Jhonatan
 * Hace la petición al ApiService y le regresa los datos al presentador.
 */
class clsLibroModelo {

    // Lista de libros. Búsqueda vacía = todos; idCategoria 0 = todas las categorías.
    suspend fun obtenerCatalogo(busqueda: String, idCategoria: Int): List<dataLibros> {
        val q = if (busqueda.isBlank()) null else busqueda.trim()
        val categoria = if (idCategoria > 0) idCategoria else null
        val respuesta = RetrofitCliente.apiService.obtenerCatalogo(q, categoria) // Aquí hace el pedido a la BD
        return respuesta.data // Retorna la lista directamente
    }

    // Categorías para el Spinner: se sacan de los libros del catálogo
    // (así no hace falta otra API). La primera opción es "Todas las categorías".
    suspend fun obtenerCategorias(): List<dataCategorias> {
        val libros = obtenerCatalogo("", 0)
        val categorias = libros
            .map { libro -> dataCategorias(libro.idCategoria, libro.categoria) }
            .distinctBy { it.id }
            .sortedBy { it.categoria }

        val listaFinal = mutableListOf(dataCategorias(0, "Todas las categorías"))
        listaFinal.addAll(categorias)
        return listaFinal
    }

    // Detalle de un libro por su id
    suspend fun obtenerDetalleLibro(idLibro: Int): dataDetalleLibro? {
        val respuesta = RetrofitCliente.apiService.obtenerDetalleLibro(idLibro)
        return respuesta.data
    }
}
