package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.modelo.LoginRequest
import com.example.app_sibcap.modelo.LoginResponse
import com.example.app_sibcap.modelo.RegistroRequest
import com.example.app_sibcap.modelo.RegistroResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse

    //EndPoint para el registro de un usuario
    @POST("registro.php")
    suspend fun regitrarLector(@Body request: RegistroRequest): RegistroResponse


}