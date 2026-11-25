package com.example.app_sibcap.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Data classes de libros — Responsable: Jhonatan
 * Los nombres de @SerialName coinciden con el JSON de catalogo.php y detalle_libro.php
 * (van dentro del "data" de ApiResponse)
 */

// Un libro del catálogo (lo que se ve en cada tarjeta del RecyclerView)
@Serializable
data class dataLibros(
    @SerialName("id_libro") val idLibro: Int = 0,
    @SerialName("titulo") val titulo: String = "",
    @SerialName("autor") val autor: String = "",
    @SerialName("id_categoria") val idCategoria: Int = 0,
    @SerialName("categoria") val categoria: String = "",
    @SerialName("portada_url") val portadaUrl: String? = null,
    @SerialName("ejemplares_disponibles") val ejemplaresDisponibles: Int = 0,
    @SerialName("disponible") val disponible: Boolean = false
)

// Todos los datos de un libro (pantalla Detalle del libro)
@Serializable
data class dataDetalleLibro(
    @SerialName("id_libro") val idLibro: Int = 0,
    @SerialName("isbn") val isbn: String = "",
    @SerialName("titulo") val titulo: String = "",
    @SerialName("sinopsis") val sinopsis: String? = null,
    @SerialName("portada_url") val portadaUrl: String? = null,
    @SerialName("autor") val autor: String = "",
    @SerialName("categoria") val categoria: String = "",
    @SerialName("editorial") val editorial: String = "",
    @SerialName("ejemplares_total") val ejemplaresTotal: Int = 0,
    @SerialName("ejemplares_disponibles") val ejemplaresDisponibles: Int = 0,
    @SerialName("disponible") val disponible: Boolean = false
)

// Categoría para el Spinner del Catálogo (id 0 = "Todas las categorías")
data class dataCategorias(
    val id: Int,
    val categoria: String
) {
    // El ArrayAdapter del Spinner muestra lo que regresa toString()
    override fun toString(): String = categoria
}

// Las respuestas de catalogo.php y detalle_libro.php usan ApiResponse<T> (ApiResponse.kt, formato del manual)
