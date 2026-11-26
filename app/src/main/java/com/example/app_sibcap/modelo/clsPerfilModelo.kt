package com.example.app_sibcap.modelo

class clsPerfilModelo {

    // Consultar información del lector
    suspend fun obtenerPerfil(token: String): PerfilResponse {
        return RetrofitCliente.apiService.obtenerPerfil(
            "Bearer $token"
        )
    }

    // Actualizar información del lector
    suspend fun actualizarPerfil(
        token: String,
        usuario: String,
        correo: String,
        telefono: String
    ): PerfilResponse {

        val datos = ActualizarPerfilRequest(
            nombreUsuario = usuario,
            correo = correo,
            telefono = telefono
        )

        return RetrofitCliente.apiService.actualizarPerfil(
            "Bearer $token",
            datos
        )
    }
}