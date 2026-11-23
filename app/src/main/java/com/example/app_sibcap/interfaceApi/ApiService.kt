package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.modelo.LoginRequest
import com.example.app_sibcap.modelo.LoginResponse
import com.example.app_sibcap.modelo.dataRespuestaCatalogo
import com.example.app_sibcap.modelo.dataRespuestaDetalle
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse
    //EndPoint para el registro de un usuario

    //EndPoint para el catálogo de libros (Jhonatan). Los parámetros en null no se mandan.
    @GET("catalogo.php")
    suspend fun obtenerCatalogo(
        @Query("q") busqueda: String?,
        @Query("id_categoria") idCategoria: Int?,
        @Query("limite") limite: Int = 50
    ): dataRespuestaCatalogo

    //EndPoint para el detalle de un libro (Jhonatan)
    @GET("detalle_libro.php")
    suspend fun obtenerDetalleLibro(@Query("id_libro") idLibro: Int): dataRespuestaDetalle
}
