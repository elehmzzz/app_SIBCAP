package com.example.app_sibcap.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActualizarPerfilRequest(

    @SerialName("nombre_usuario")
    val nombreUsuario: String,

    @SerialName("correo")
    val correo: String,

    @SerialName("telefono")
    val telefono: String
)