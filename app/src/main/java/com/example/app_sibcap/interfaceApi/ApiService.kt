package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.MODELO.LoginRequest
import com.example.app_sibcap.MODELO.LoginResponse
import com.example.app_sibcap.VISTA.LoginActivity
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse
    //EndPoint para el registro de un usuario

}