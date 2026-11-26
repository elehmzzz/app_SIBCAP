package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.PrestamoDetalle

interface DetallePrestamoContract {

    interface View {
        fun mostrarCarga()
        fun ocultarCarga()
        fun mostrarDetalle(detalle: PrestamoDetalle)
        fun mostrarError(mensaje: String, sinConexion: Boolean)
        fun sesionExpirada()
    }

    interface Presenter {
        fun cargarDetalle(idPrestamo: Int)
        fun reintentar()
        fun destruir()
    }
}
