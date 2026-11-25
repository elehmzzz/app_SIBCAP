package com.example.app_sibcap.presentador

import android.content.Intent
import com.example.app_sibcap.vista.BienvenidaActivity
import com.example.app_sibcap.vista.CatalogoActivity

/*
 * Bienvenida multimedia — Responsable: Jhonatan
 * No requiere modelo propio ni servicio PHP (tabla Fase 1).
 * El video y el audio son archivos que se suben por FTP a la carpeta upload/ del servidor.
 */
class clsBienvenidaPresenter(val vista: BienvenidaActivity) {

    private val urlVideo = "https://sibcap.com/app_sibcap/upload/bienvenida.mp4"
    private val urlAudio = "https://sibcap.com/app_sibcap/upload/bienvenida.mp3"

    private var nombreUsuario: String? = null
    private var audioSonando = false

    fun iniciar(nombre: String?) {
        // Se guarda para mandarlo al Catálogo ("¡Bienvenido(a), Valeria!")
        nombreUsuario = nombre
    }

    // ---- Video ----
    fun reproducirVideo() {
        vista.iniciarVideo(urlVideo)
    }

    fun errorVideo() {
        vista.mostrarErrorVideo()
        vista.mostrarError("No se pudo reproducir el video. Revisa tu conexión.")
    }

    // ---- Audio: el mismo botón reproduce y pausa ----
    fun alternarAudio() {
        if (audioSonando) {
            audioSonando = false
            vista.pausarAudio()
        } else {
            audioSonando = true
            vista.reproducirAudio(urlAudio)
        }
    }

    fun audioTerminado() {
        audioSonando = false
        vista.mostrarAudioDetenido()
    }

    fun errorAudio() {
        audioSonando = false
        vista.mostrarAudioDetenido()
        vista.mostrarError("No se pudo reproducir la guía en audio.")
    }

    // "Ir a mi catálogo" y "Omitir introducción" llevan al Catálogo.
    // Crear y lanzar el Intent directamente desde el Presentador
    fun irACatalogo() {
        val intent = Intent(vista, CatalogoActivity::class.java)
        intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
        vista.startActivity(intent)
        vista.finish() // la bienvenida ya no se vuelve a mostrar al regresar
    }
}
