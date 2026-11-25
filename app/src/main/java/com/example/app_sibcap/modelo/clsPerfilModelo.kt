package com.example.app_sibcap.modelo

class clsPerfilModelo {

    suspend fun obtenerPerfil(token: String): PerfilResponse {

        return RetrofitCliente.apiService.obtenerPerfil(
            "Bearer $token"
        )
    }
}
