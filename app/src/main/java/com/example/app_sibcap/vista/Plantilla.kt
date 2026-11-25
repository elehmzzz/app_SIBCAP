package com.example.app_sibcap.vista

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_sibcap.R

/*
 * PLANTILLA SIBCAP (compartida por todo el equipo)
 * Encabezado verde + barra de navegación inferior.
 *
 * En tu XML (raíz con android:id="@+id/main"):
 *     <include android:id="@+id/encabezado" layout="@layout/layout_encabezado" />
 *     ... tu contenido ...
 *     <include android:id="@+id/navegacion" layout="@layout/layout_navegacion" />
 *
 * En tu Activity, después de setContentView (en lugar del bloque de ViewCompat):
 *     Plantilla.aplicarMargenes(this)
 *     Plantilla.configurarNavegacion(this, Plantilla.PRESTAMOS)   // la sección de tu pantalla
 *
 * Si tu pantalla usa layout_encabezado_regresar:
 *     Plantilla.configurarRegreso(this, "Detalle del préstamo")
 */
object Plantilla {

    const val CATALOGO = 0
    const val PRESTAMOS = 1
    const val PERFIL = 2

    // Nombres de las Activities de cada sección (tabla de la Fase 1)
    private const val ACTIVITY_CATALOGO = "com.example.app_sibcap.vista.CatalogoActivity"
    private const val ACTIVITY_PRESTAMOS = "com.example.app_sibcap.vista.PrestamosActivity"
    private const val ACTIVITY_PERFIL = "com.example.app_sibcap.vista.PerfilActivity"

    // El encabezado verde se pinta también detrás de la barra de estado del celular
    fun aplicarMargenes(activity: AppCompatActivity) {
        val raiz = activity.findViewById<View>(R.id.main)
        val encabezado = activity.findViewById<View>(R.id.encabezado)

        // Íconos de la barra de estado en blanco (porque el encabezado es verde oscuro)
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            .isAppearanceLightStatusBars = false

        ViewCompat.setOnApplyWindowInsetsListener(raiz) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            if (encabezado != null) {
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                encabezado.setPadding(0, systemBars.top, 0, 0)
            } else {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            }
            insets
        }
    }

    // Para layout_encabezado_regresar: pone el título y la flecha cierra la pantalla
    fun configurarRegreso(activity: AppCompatActivity, titulo: String) {
        activity.findViewById<TextView>(R.id.txtEncabezadoTitulo).text = titulo
        activity.findViewById<ImageButton>(R.id.btnEncabezadoRegresar).setOnClickListener {
            activity.finish()
        }
    }

    // Marca la sección actual y abre las otras pantallas al tocar los íconos
    fun configurarNavegacion(activity: AppCompatActivity, seccionActual: Int) {
        activity.findViewById<View>(R.id.indicadorCatalogo).visibility =
            if (seccionActual == CATALOGO) View.VISIBLE else View.INVISIBLE
        activity.findViewById<View>(R.id.indicadorPrestamos).visibility =
            if (seccionActual == PRESTAMOS) View.VISIBLE else View.INVISIBLE
        activity.findViewById<View>(R.id.indicadorPerfil).visibility =
            if (seccionActual == PERFIL) View.VISIBLE else View.INVISIBLE

        activity.findViewById<View>(R.id.navCatalogo).setOnClickListener { abrir(activity, ACTIVITY_CATALOGO) }
        activity.findViewById<View>(R.id.navPrestamos).setOnClickListener { abrir(activity, ACTIVITY_PRESTAMOS) }
        activity.findViewById<View>(R.id.navPerfil).setOnClickListener { abrir(activity, ACTIVITY_PERFIL) }
    }

    // Se abre por nombre para que compile aunque la pantalla de otro compañero aún no exista
    private fun abrir(activity: AppCompatActivity, nombreActivity: String) {
        if (activity.javaClass.name == nombreActivity) return // ya estás en esa pantalla
        try {
            val intent = Intent()
            intent.setClassName(activity, nombreActivity)
            // Si la pantalla ya estaba abierta, regresa a ella en lugar de abrir otra encima
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Esta sección aún no está disponible.", Toast.LENGTH_SHORT).show()
        }
    }
}
