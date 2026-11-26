package com.example.app_sibcap.modelo

import android.content.Context

// Guarda y lee el token que devuelve login.php.
// El login debe llamar a guardar() cuando "exito" sea true. Nunca se escribe el token en Logcat.
class SesionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("sibcap_sesion", Context.MODE_PRIVATE)

    fun guardar(token: String, tipoUsuario: String, nombre: String) {
        prefs.edit()
            .putString("token", token)
            .putString("tipo_usuario", tipoUsuario)
            .putString("nombre", nombre)
            .apply()
    }

    fun token(): String? = prefs.getString("token", null)

    fun tipoUsuario(): String? = prefs.getString("tipo_usuario", null)

    fun nombre(): String? = prefs.getString("nombre", null)

    fun cerrar() {
        prefs.edit().clear().apply()
    }
}
