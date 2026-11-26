package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.Prestamo
import com.example.app_sibcap.modelo.ResumenPrestamos

enum class FiltroPrestamos { TODOS, VIGENTES, VENCIDOS }

interface MisPrestamosContract {

    interface View {
        fun mostrarCarga()
        fun ocultarCarga()
        fun mostrarResumen(resumen: ResumenPrestamos, librosVencidos: Int)
        fun mostrarPrestamos(prestamos: List<Prestamo>)
        // sinPrestamos = true: el lector no tiene ningún préstamo (estado "Aún no tienes préstamos").
        // sinPrestamos = false: el filtro elegido no tiene resultados.
        fun mostrarVacio(mensaje: String, sinPrestamos: Boolean)
        fun mostrarError(mensaje: String, sinConexion: Boolean)
        fun sesionExpirada()
    }

    interface Presenter {
        fun cargarPrestamos()
        fun filtrar(filtro: FiltroPrestamos)
        fun reintentar()
        fun destruir()
    }
}
