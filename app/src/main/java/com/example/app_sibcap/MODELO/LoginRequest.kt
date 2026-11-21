package com.example.app_sibcap.MODELO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class LoginRequest (
    //aunque el la variable sea "usuario" tambien puede se el correo
    @SerialName("usuario") val nombreUsuario: String = "",
    @SerialName("contrasenia") val contrasenia: String = "")