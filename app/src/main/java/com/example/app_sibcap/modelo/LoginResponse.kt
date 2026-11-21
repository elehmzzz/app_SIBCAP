package com.example.app_sibcap.modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse (
    @SerialName("exito") val exito: Boolean = false,
    @SerialName("mensaje") val mensaje: String = "",
    @SerialName("token") val token: String? = null,
    @SerialName("nombre") val nombre: String? = null,
    @SerialName("tipoUsuario") val tipoUsuario: String? = null,
    @SerialName("rol") val rol: String? = null, )