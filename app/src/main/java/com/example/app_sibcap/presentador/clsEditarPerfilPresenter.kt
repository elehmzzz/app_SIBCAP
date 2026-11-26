package com.example.app_sibcap.presentador

import android.util.Patterns
import com.example.app_sibcap.vista.EditarPerfilActivity

class clsEditarPerfilPresenter(
    private val vista: EditarPerfilActivity
) {

    fun guardarCambios(
        usuario: String,
        correo: String,
        telefono: String
    ) {

        // Validar nombre de usuario
        if (usuario.isBlank()) {
            vista.mostrarError("Ingresa tu nombre de usuario.")
            return
        }

        if (usuario.length > 50) {
            vista.mostrarError(
                "El usuario no debe superar los 50 caracteres."
            )
            return
        }

        // Validar correo electrónico
        if (correo.isBlank()) {
            vista.mostrarError("Ingresa tu correo electrónico.")
            return
        }

        if (correo.length > 100) {
            vista.mostrarError(
                "El correo no debe superar los 100 caracteres."
            )
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            vista.mostrarError("El correo electrónico no es válido.")
            return
        }

        // Validar teléfono opcional
        if (telefono.isNotEmpty() &&
            !telefono.matches(Regex("^[0-9+\\s()-]{7,15}$"))
        ) {
            vista.mostrarError("El teléfono no tiene un formato válido.")
            return
        }

        vista.mostrarConfirmacion(usuario, correo, telefono)
    }

    fun confirmarEdicion(
        usuario: String,
        correo: String,
        telefono: String
    ) {
        // Pendiente: actualizar información mediante el modelo.
    }
}