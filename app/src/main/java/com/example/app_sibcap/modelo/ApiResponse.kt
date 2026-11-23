package com.example.app_sibcap.modelo



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Formato acordado en el manual: { "status": "success" | "error", "message": "...", "data": ... }
@Serializable
data class ApiResponse<T>(
    @SerialName("status") val status: String = "",
    @SerialName("message") val message: String = "",
    @SerialName("data") val data: T? = null
) {
    val esExito: Boolean get() = status == "success"
}

// Cuerpo de los errores HTTP (401, 403, 404, 500...)
@Serializable
data class ApiError(
    @SerialName("status") val status: String = "",
    @SerialName("message") val message: String = ""
)
