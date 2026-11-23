package com.example.app_sibcap.modelo

class clsLoginModelo {
    suspend fun autenticarUsuario(usuario:String, clave:String): LoginResponse{
        val request = LoginRequest(usuario, clave)
        return RetrofitCliente.apiService.iniciarSesion(request)
    }
}