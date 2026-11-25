package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.modelo.LoginRequest
import com.example.app_sibcap.modelo.LoginResponse
import com.example.app_sibcap.modelo.PerfilResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse
    //EndPoint para el registro de un usuario

    // EndPoint para consultar el perfil del lector
    @GET("perfil.php")
    suspend fun obtenerPerfil(@Header("Authorization") token: String): PerfilResponse
}