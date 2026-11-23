package com.example.app_sibcap.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ---------- mis_prestamos.php ----------
@Serializable
data class PrestamosData(
    @SerialName("resumen") val resumen: ResumenPrestamos = ResumenPrestamos(),
    @SerialName("prestamos") val prestamos: List<Prestamo> = emptyList()
)

@Serializable
data class ResumenPrestamos(
    @SerialName("total_prestamos") val totalPrestamos: Int = 0,
    @SerialName("prestamos_activos") val prestamosActivos: Int = 0,
    @SerialName("max_libros") val maxLibros: Int = 0,
    @SerialName("total_multa_pendiente") val totalMultaPendiente: Double = 0.0,
    @SerialName("multa_por_dia") val multaPorDia: Double = 0.0
)

@Serializable
data class Prestamo(
    @SerialName("id_prestamo") val idPrestamo: Int = 0,
    @SerialName("id_estatus") val idEstatus: Int = 0,
    @SerialName("estatus") val estatus: String = "",
    @SerialName("folio") val folio: String = "",
    @SerialName("titulo") val titulo: String = "",
    @SerialName("autor") val autor: String = "",
    @SerialName("portada_url") val portadaUrl: String? = null,
    @SerialName("fecha_prestamo") val fechaPrestamo: String = "",
    @SerialName("fecha_devolucion") val fechaDevolucion: String = "",
    @SerialName("fecha_entrega") val fechaEntrega: String? = null,
    @SerialName("dias_restantes") val diasRestantes: Int? = null,   // null si ya no está activo
    @SerialName("dias_retraso") val diasRetraso: Int = 0,
    @SerialName("multa") val multa: Double = 0.0,                   // calculada por el servidor
    @SerialName("vencido") val vencido: Boolean = false
)

// ---------- prestamo_detalle.php ----------
@Serializable
data class ReglasPrestamo(
    @SerialName("multa_por_dia") val multaPorDia: Double = 0.0,
    @SerialName("dias_tolerancia") val diasTolerancia: Int = 0,
    @SerialName("dias_prestamo") val diasPrestamo: Int = 0
)

@Serializable
data class PrestamoDetalle(
    @SerialName("id_prestamo") val idPrestamo: Int = 0,
    @SerialName("id_estatus") val idEstatus: Int = 0,
    @SerialName("estatus") val estatus: String = "",
    @SerialName("folio") val folio: String = "",
    @SerialName("isbn") val isbn: String = "",
    @SerialName("titulo") val titulo: String = "",
    @SerialName("sinopsis") val sinopsis: String? = null,
    @SerialName("portada_url") val portadaUrl: String? = null,
    @SerialName("autor") val autor: String = "",
    @SerialName("categoria") val categoria: String = "",
    @SerialName("editorial") val editorial: String = "",
    @SerialName("bibliotecario") val bibliotecario: String = "",
    @SerialName("fecha_prestamo") val fechaPrestamo: String = "",
    @SerialName("fecha_devolucion") val fechaDevolucion: String = "",
    @SerialName("fecha_entrega") val fechaEntrega: String? = null,
    @SerialName("dias_restantes") val diasRestantes: Int? = null,
    @SerialName("dias_retraso") val diasRetraso: Int = 0,
    @SerialName("multa") val multa: Double = 0.0,
    @SerialName("vencido") val vencido: Boolean = false,
    @SerialName("multa_pagada") val multaPagada: Boolean = false,
    @SerialName("multa_pendiente") val multaPendiente: Boolean = false,
    @SerialName("reglas") val reglas: ReglasPrestamo = ReglasPrestamo()
)
