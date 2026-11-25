package com.example.app_sibcap.interfaceApi

import com.example.app_sibcap.modelo.ApiResponse
import com.example.app_sibcap.modelo.LoginRequest
import com.example.app_sibcap.modelo.LoginResponse
import com.example.app_sibcap.modelo.RegistroRequest
import com.example.app_sibcap.modelo.RegistroResponse
import com.example.app_sibcap.modelo.dataDetalleLibro
import com.example.app_sibcap.modelo.dataLibros
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //EndPoint para el login a la api
    @POST("login.php")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse

    //EndPoint para el registro de un usuario Tello?
    @POST("registro.php")
    suspend fun regitrarLector(@Body request: RegistroRequest): RegistroResponse

    //EndPoint para el catálogo de libros (Jhonatan). Los parámetros en null no se mandan.
    @GET("catalogo.php")
    suspend fun obtenerCatalogo(
        @Query("q") busqueda: String?,
        @Query("id_categoria") idCategoria: Int?,
        @Query("limite") limite: Int = 50
    ): ApiResponse<List<dataLibros>>

    //EndPoint para el detalle de un libro (Jhonatan)
    @GET("detalle_libro.php")
    suspend fun obtenerDetalleLibro(@Query("id_libro") idLibro: Int): ApiResponse<dataDetalleLibro>


}