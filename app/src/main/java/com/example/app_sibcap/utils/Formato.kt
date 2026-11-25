package com.example.app_sibcap.utils

import java.util.Locale

// Formatos de texto compartidos por las pantallas de préstamos
object Formato {

    // "2026-11-24" -> "24/11/2026"
    fun fecha(iso: String?): String {
        if (iso.isNullOrBlank()) return "—"
        val partes = iso.split("-")
        return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else iso
    }

    // 20.0 -> "$20.00"
    fun moneda(valor: Double): String = String.format(Locale.US, "\$%.2f", valor)

    // 50.0 -> "$50.00 MXN"
    fun mxn(valor: Double): String = String.format(Locale.US, "\$%.2f MXN", valor)

    // 2 -> "P002" (código de préstamo que muestra el diseño)
    fun codigoPrestamo(id: Int): String = String.format(Locale.US, "P%03d", id)
}
