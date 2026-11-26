package com.example.app_sibcap.vista

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_sibcap.R
import com.example.app_sibcap.presentador.clsBienvenidaPresenter

/*
 * Bienvenida multimedia — Responsable: Jhonatan
 * Se abre desde el Login:
 *   val intent = Intent(this, BienvenidaActivity::class.java)
 *   intent.putExtra("NOMBRE_USUARIO", respuesta.nombre)
 *   startActivity(intent)
 */
class BienvenidaActivity : AppCompatActivity() {

    private lateinit var frameVideo: FrameLayout
    private lateinit var videoBienvenida: VideoView
    private lateinit var layoutPortadaVideo: LinearLayout
    private lateinit var txtVideo: TextView
    private lateinit var pbVideo: ProgressBar
    private lateinit var layoutAudio: LinearLayout
    private lateinit var imgAudio: ImageView
    private lateinit var txtAudio: TextView
    private lateinit var pbAudio: ProgressBar
    private lateinit var btnIrCatalogo: Button
    private lateinit var txtOmitir: TextView

    private var reproductorAudio: MediaPlayer? = null

    //creamos nuestro objeto presentador
    private lateinit var presentador: clsBienvenidaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        // Encabezado verde compartido (reemplaza el bloque de ViewCompat)
        Plantilla.aplicarMargenes(this)

        frameVideo = findViewById(R.id.frameVideo)
        videoBienvenida = findViewById(R.id.videoBienvenida)
        layoutPortadaVideo = findViewById(R.id.layoutPortadaVideo)
        txtVideo = findViewById(R.id.txtVideo)
        pbVideo = findViewById(R.id.pbVideo)
        layoutAudio = findViewById(R.id.layoutAudio)
        imgAudio = findViewById(R.id.imgAudio)
        txtAudio = findViewById(R.id.txtAudio)
        pbAudio = findViewById(R.id.pbAudio)
        btnIrCatalogo = findViewById(R.id.btnIrCatalogo)
        txtOmitir = findViewById(R.id.txtOmitir)

        //inicializar al presentador
        presentador = clsBienvenidaPresenter(this)

        //crear los listeners
        frameVideo.setOnClickListener(this::ejecutarVideo)
        layoutAudio.setOnClickListener(this::ejecutarAudio)
        btnIrCatalogo.setOnClickListener(this::ejecutarIrCatalogo)
        txtOmitir.setOnClickListener(this::ejecutarIrCatalogo)

        // Recuperar el nombre enviado desde el Login
        presentador.iniciar(intent.getStringExtra("NOMBRE_USUARIO"), intent.getStringExtra("USUARIO"))
    }//cierra el oncreate

    fun ejecutarVideo(v: View) {
        presentador.reproducirVideo()
    }

    fun ejecutarAudio(v: View) {
        presentador.alternarAudio()
    }

    fun ejecutarIrCatalogo(v: View) {
        presentador.irACatalogo()
    }

    //Salidas
    fun iniciarVideo(url: String) {
        // Si ya se está viendo, el toque lo manejan los controles del video
        if (videoBienvenida.visibility == View.VISIBLE) return

        layoutPortadaVideo.visibility = View.GONE
        pbVideo.visibility = View.VISIBLE
        videoBienvenida.visibility = View.VISIBLE

        // Controles de play, pausa y barra de progreso
        val controles = MediaController(this)
        controles.setAnchorView(videoBienvenida)
        videoBienvenida.setMediaController(controles)

        videoBienvenida.setOnPreparedListener {
            pbVideo.visibility = View.GONE
            videoBienvenida.start()
        }
        videoBienvenida.setOnErrorListener { _, _, _ ->
            presentador.errorVideo()
            true
        }
        videoBienvenida.setVideoURI(Uri.parse(url))
    }

    fun mostrarErrorVideo() {
        videoBienvenida.stopPlayback()
        videoBienvenida.visibility = View.GONE
        pbVideo.visibility = View.GONE
        layoutPortadaVideo.visibility = View.VISIBLE
        txtVideo.text = "Video no disponible"
    }

    fun reproducirAudio(url: String) {
        val reproductor = reproductorAudio
        if (reproductor != null) {
            // Ya estaba cargado: solo continúa
            reproductor.start()
            mostrarAudioSonando()
            return
        }

        pbAudio.visibility = View.VISIBLE
        val nuevo = MediaPlayer()
        nuevo.setAudioAttributes(
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()
        )
        nuevo.setOnPreparedListener {
            pbAudio.visibility = View.GONE
            it.start()
            mostrarAudioSonando()
        }
        nuevo.setOnCompletionListener { presentador.audioTerminado() }
        nuevo.setOnErrorListener { _, _, _ ->
            liberarAudio()
            presentador.errorAudio()
            true
        }
        try {
            nuevo.setDataSource(url)
            nuevo.prepareAsync()
            reproductorAudio = nuevo
        } catch (e: Exception) {
            nuevo.release()
            presentador.errorAudio()
        }
    }

    fun pausarAudio() {
        reproductorAudio?.let { if (it.isPlaying) it.pause() }
        mostrarAudioDetenido()
    }

    fun mostrarAudioDetenido() {
        pbAudio.visibility = View.GONE
        imgAudio.setImageResource(R.drawable.ic_audio)
        txtAudio.text = "Escuchar guía en audio"
    }

    private fun mostrarAudioSonando() {
        imgAudio.setImageResource(R.drawable.ic_pausa)
        txtAudio.text = "Pausar guía en audio"
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun liberarAudio() {
        reproductorAudio?.release()
        reproductorAudio = null
    }

    override fun onPause() {
        super.onPause()
        if (videoBienvenida.isPlaying) videoBienvenida.pause()
        if (reproductorAudio?.isPlaying == true) presentador.alternarAudio() // pausa el audio
    }

    override fun onDestroy() {
        liberarAudio()
        super.onDestroy()
    }
}
