package com.example.app_sibcap.presentador

import android.util.Patterns
import com.example.app_sibcap.vista.EditarPerfilActivity
import androidx.lifecycle.lifecycleScope
import com.example.app_sibcap.modelo.clsPerfilModelo
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class clsEditarPerfilPresenter(
    private val vista: EditarPerfilActivity
) {

    private val modelo = clsPerfilModelo()
    private var operacionEnCurso = false

    fun cargarDatos(token: String) {

        if (operacionEnCurso) return

        operacionEnCurso = true

        vista.lifecycleScope.launch {

            vista.mostrarCarga("Cargando información…")

            try {
                val respuesta = modelo.obtenerPerfil(token)

                if (respuesta.success && respuesta.data != null) {
                    vista.mostrarDatos(respuesta.data)
                } else {
                    vista.mostrarError(respuesta.message)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {

                val mensaje = when (e.code()) {
                    401 -> "Tu sesión ha vencido. Inicia sesión nuevamente."
                    404 -> "No se encontró el perfil del lector."
                    else -> "No fue posible consultar tus datos."
                }

                vista.mostrarError(mensaje)

            } catch (e: IOException) {

                vista.mostrarError(
                    "Verifica tu conexión a Internet."
                )

            } catch (e: SerializationException) {

                vista.mostrarError(
                    "No fue posible interpretar la respuesta del servidor."
                )

            } finally {
                operacionEnCurso = false

                if (!vista.isFinishing && !vista.isDestroyed) {
                    vista.ocultarCarga()
                }
            }
        }
    }
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
        // Pendiente: obtener el token de sesión.
    }

    private fun actualizarPerfil(
        token: String,
        usuario: String,
        correo: String,
        telefono: String
    ) {

        if (operacionEnCurso) return

        operacionEnCurso = true

        vista.lifecycleScope.launch {

            vista.mostrarCarga("Guardando cambios…")

            try {

                val respuesta = modelo.actualizarPerfil(
                    token,
                    usuario,
                    correo,
                    telefono
                )

                if (respuesta.success && respuesta.data != null) {
                    vista.ocultarCarga()
                    vista.mostrarEdicionExitosa()
                } else {
                    vista.mostrarErrorGuardado(respuesta.message)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {

                val mensaje = when (e.code()) {
                    400 -> "Los datos proporcionados no son válidos."
                    401 -> "Tu sesión ha vencido. Inicia sesión nuevamente."
                    404 -> "No se encontró el perfil del lector."
                    409 -> "El correo o nombre de usuario ya está registrado."
                    else -> "No fue posible actualizar el perfil."
                }

                if (e.code() == 401 || e.code() == 404) {
                    vista.mostrarError(mensaje)
                } else {
                    vista.mostrarErrorGuardado(mensaje)
                }

            } catch (e: IOException) {

                vista.mostrarErrorGuardado(
                    "No fue posible confirmar los cambios. Verifica tu conexión."
                )

            } catch (e: SerializationException) {

                vista.mostrarErrorGuardado(
                    "No fue posible confirmar los cambios. La respuesta del servidor no es válida."
                )

            } finally {
                operacionEnCurso = false

                if (!vista.isFinishing && !vista.isDestroyed) {
                    vista.ocultarCarga()
                }
            }
            }
        }
    }
