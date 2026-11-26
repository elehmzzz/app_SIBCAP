package com.example.app_sibcap.modelo

import android.content.Context

class clsPreferenciasModelo(context: Context) {
    private val prefs = context.getSharedPreferences("sibcap_prefs", Context.MODE_PRIVATE)

    fun yaVioBienvenida(usuario: String): Boolean {
        return prefs.getBoolean("bienvenida_" + usuario.lowercase(), false)
    }

    fun marcarBienvenidaVista(usuario: String) {
        prefs.edit().putBoolean("bienvenida_" + usuario.lowercase(), true).apply()
    }
}