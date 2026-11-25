package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.modelo.ApiResponse
import com.example.app_sibcap.modelo.LoginRequest
import com.example.app_sibcap.modelo.LoginResponse
import com.example.app_sibcap.modelo.PrestamoDetalle
import com.example.app_sibcap.modelo.PrestamosData
import com.example.app_sibcap.modelo.RegistroRequest
import com.example.app_sibcap.modelo.RegistroResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse

    //EndPoint para el registro de un usuario
    @POST("registro.php")
    suspend fun regitrarLector(@Body request: RegistroRequest): RegistroResponse

    //EndPoint: préstamos del lector autenticado (con días restantes, días de retraso y multa)
    @GET("mis_prestamos.php")
    suspend fun misPrestamos(
        @Header("Authorization") autorizacion: String   // "Bearer <token>"
    ): ApiResponse<PrestamosData>

    //EndPoint: detalle de un préstamo del lector
    @GET("prestamo_detalle.php")
    suspend fun prestamoDetalle(
        @Header("Authorization") autorizacion: String,  // "Bearer <token>"
        @Query("id_prestamo") idPrestamo: Int
    ): ApiResponse<PrestamoDetalle>
}