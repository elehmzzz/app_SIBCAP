package com.example.app_sibcap.MODELO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class LoginRequest (
    @SerialName("usuario") val nombreUsuario: String = "",
    @SerialName("contrasenia") val contrasenia: String = "")