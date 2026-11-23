package com.example.app_sibcap.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_sibcap.R
import com.example.app_sibcap.presentador.clsLoginPresenter

class LoginActivity : AppCompatActivity() {

    private lateinit var presentador: clsLoginPresenter

    //declaro las variables que se van a usar
    private lateinit var edtUsuario: EditText
    private lateinit var edtContrasenia: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var lnkRegistroLector: TextView
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //enlazamos los componentes del XML
        edtUsuario = findViewById(R.id.edtNombreUsuario)
        edtContrasenia = findViewById(R.id.edtContrasenia)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        //link para ir al registro
        lnkRegistroLector = findViewById(R.id.txtRegistrate)
        //inicializa el presentador
        presentador = clsLoginPresenter(this)

        //listener del boton
        btnIniciarSesion.setOnClickListener(this::ejecutarInicioSesion)


    }

    fun ejecutarInicioSesion(v: View){
        val usuario = edtUsuario.text.toString().trim()
        val contrasenia = edtContrasenia.text.toString().trim()
        presentador.realizarLogin(usuario,contrasenia)
    }


    //funcion cuando el login es exitoso
    fun loginExitoso(mensaje: String, nombreCliente:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_LONG).show()
        //se manda a llamar la vista
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    //funcion que recibe el mensaje de error, enviado de presentador
    fun mostrarError(mensaje:String){
        Toast.makeText(this,mensaje, Toast.LENGTH_LONG).show()
    }
}