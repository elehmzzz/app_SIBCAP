package com.example.app_sibcap.modelo

// Lo que el repositorio le entrega al Presenter: o hay datos, o hay un mensaje comprensible.
// codigo = código HTTP (401, 403, 404, 500...) o 0 si no hubo respuesta.
// sinConexion = true cuando no hubo internet o se agotó el tiempo de espera.
sealed class ResultadoApi<out T> {
    data class Exito<T>(val datos: T) : ResultadoApi<T>()
    data class Error(
        val codigo: Int,
        val mensaje: String,
        val sinConexion: Boolean = false
    ) : ResultadoApi<Nothing>()
}
