package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.clsLibroModelo
import com.example.app_sibcap.vista.DetalleLibroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * Detalle del libro — Responsable: Jhonatan
 * Modelo: clsLibroModelo   Servicio: detalle_libro.php
 */
class clsDetalleLibroPresenter(private val vista: DetalleLibroActivity) {

    private val modelo = clsLibroModelo()

    fun cargarDetalle(idLibro: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            vista.mostrarCargando(true)
            try {
                // Aquí se consume obtenerDetalleLibro del modelo
                val libro = modelo.obtenerDetalleLibro(idLibro)
                vista.mostrarCargando(false)

                if (libro == null) {
                    vista.mostrarError("El libro no existe.")
                    return@launch
                }

                vista.mostrarDetalle(libro, formatearIsbn(libro.isbn))
            } catch (e: Exception) {
                vista.mostrarCargando(false)
                vista.mostrarError("Error al cargar detalle: " + e.localizedMessage)
            }
        }
    }

    // 9780307474728 -> 978-0307474728 (como en el diseño)
    private fun formatearIsbn(isbn: String): String {
        return if (isbn.length == 13 && (isbn.startsWith("978") || isbn.startsWith("979"))) {
            isbn.substring(0, 3) + "-" + isbn.substring(3)
        } else {
            isbn
        }
    }
}
