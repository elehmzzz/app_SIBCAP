
package com.example.app_sibcap.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilResponse(

    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val data: DatosPerfil? = null
)

@Serializable
data class DatosPerfil(

    @SerialName("id_lector")
    val idLector: Int,

    @SerialName("nombres")
    val nombres: String,

    @SerialName("apellido_paterno")
    val apellidoPaterno: String,

    @SerialName("apellido_materno")
    val apellidoMaterno: String? = null,

    @SerialName("correo")
    val correo: String,

    @SerialName("nombre_usuario")
    val nombreUsuario: String,

    @SerialName("telefono")
    val telefono: String? = null
)
