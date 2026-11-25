package com.example.app_sibcap.presentador

import android.view.View
import com.example.app_sibcap.modelo.clsLoginModelo
import com.example.app_sibcap.vista.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class clsLoginPresenter(private val view: LoginActivity) {

    private val modelo = clsLoginModelo()

    //funcion que recibe el nombre de usuario/correo y la contraseña
    fun realizarLogin(usuario: String, clave:String){

        if(usuario.isEmpty() || clave.isEmpty()){
            view.mostrarError("Porfavor, ingresa tu usuario y tu contraseña")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try{
                val respuesta = withContext(Dispatchers.IO){
                    modelo.autenticarUsuario(usuario, clave)
                }

                if(respuesta.exito){
                    view.loginExitoso(respuesta.mensaje, respuesta.nombre)
                }else{
                    view.mostrarError(respuesta.mensaje)
                }

            }catch (e: Exception){
                view.mostrarError("Error de conexión: ${e.localizedMessage ?: "Error desconocido"}")
            }
        }

    }

}