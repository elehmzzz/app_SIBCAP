
package com.example.app_sibcap.presentador

import com.example.app_sibcap.modelo.clsPerfilModelo
import com.example.app_sibcap.vista.PerfilActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPerfilPresenter(private val vista: PerfilActivity) {

    private val modelo = clsPerfilModelo()

    fun obtenerPerfil(token: String) {

        CoroutineScope(Dispatchers.Main).launch {

            try {
                val respuesta = modelo.obtenerPerfil(token)


                if (respuesta.success && respuesta.data != null) {
                    vista.mostrarPerfil(respuesta.data)
                } else {
                    vista.mostrarError(respuesta.message)
                }

            } catch (e: Exception) {
                vista.mostrarError(
                    "No fue posible consultar el perfil. Verifica tu conexión."
                )
            }

        }
    }
}
