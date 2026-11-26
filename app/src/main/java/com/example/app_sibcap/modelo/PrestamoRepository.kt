package com.example.app_sibcap.modelo

import com.example.app_sibcap.modelo.Prestamo
import com.example.app_sibcap.utils.Formato

import com.example.app_sibcap.interfaceApi.ApiService
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

// Interfaz: permite usar un repositorio falso en las pruebas unitarias del Presenter
interface PrestamoRepository {
    suspend fun obtenerMisPrestamos(token: String): ResultadoApi<PrestamosData>
    suspend fun obtenerDetalle(token: String, idPrestamo: Int): ResultadoApi<PrestamoDetalle>
}

// Aquí se centralizan las llamadas de red de préstamos (el Presenter nunca llama a Retrofit)
class PrestamoRepositoryImpl(
    private val api: ApiService = RetrofitCliente.apiService
) : PrestamoRepository {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override suspend fun obtenerMisPrestamos(token: String): ResultadoApi<PrestamosData> =
        llamar { api.misPrestamos("Bearer $token") }

    override suspend fun obtenerDetalle(token: String, idPrestamo: Int): ResultadoApi<PrestamoDetalle> =
        llamar { api.prestamoDetalle("Bearer $token", idPrestamo) }

    // Convierte cualquier resultado o falla en ResultadoApi
    private suspend fun <T> llamar(bloque: suspend () -> ApiResponse<T>): ResultadoApi<T> {
        return try {
            val respuesta = bloque()
            val datos = respuesta.data
            if (respuesta.esExito && datos != null) {
                ResultadoApi.Exito(datos)
            } else {
                ResultadoApi.Error(0, respuesta.message.ifBlank { "Respuesta inválida del servidor." })
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            // El servidor responde los errores como JSON: {"status":"error","message":"..."}
            val cuerpo = try { e.response()?.errorBody()?.string() } catch (ex: Exception) { null }
            val mensaje = try {
                cuerpo?.let { json.decodeFromString(ApiError.serializer(), it).message }
            } catch (ex: Exception) { null }
            ResultadoApi.Error(e.code(), if (!mensaje.isNullOrBlank()) mensaje else "Error del servidor (${e.code()}).")
        } catch (e: SerializationException) {
            ResultadoApi.Error(0, "El servidor devolvió una respuesta inválida.")
        } catch (e: IOException) {
            // Sin internet o tiempo de espera agotado (texto del diseño "No pudimos conectar")
            ResultadoApi.Error(0, "Revisa tu conexión a internet e inténtalo nuevamente.", sinConexion = true)
        } catch (e: Exception) {
            ResultadoApi.Error(0, "Ocurrió un error inesperado.")
        }
    }
}
