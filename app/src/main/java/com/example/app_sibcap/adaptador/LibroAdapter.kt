package com.example.app_sibcap.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app_sibcap.R
import com.example.app_sibcap.modelo.dataLibros
import com.example.app_sibcap.vista.CatalogoActivity

/*
 * Adaptador del RecyclerView del Catálogo — Responsable: Jhonatan
 * Infla item_libro.xml por cada libro
 */
class LibroAdapter(
    private val activity: CatalogoActivity,
    private var listaLibros: List<dataLibros>
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPortada: ImageView = itemView.findViewById(R.id.imgPortada)
        val tvTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val tvAutor: TextView = itemView.findViewById(R.id.txtAutor)
        val tvCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
        val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = listaLibros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = libro.autor
        holder.tvCategoria.text = libro.categoria

        // La API ya manda la URL completa de la portada (o null si no tiene)
        Glide.with(holder.itemView.context)
            .load(libro.portadaUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // Imagen por defecto mientras carga
            .error(android.R.drawable.ic_menu_gallery)       // Imagen si falla o no tiene portada
            .into(holder.imgPortada)

        // Tocar el botón o la tarjeta abre el detalle
        val alTocar = object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Esta función se ejecuta en la vista CatalogoActivity
                // y se manda al presentador para abrir el libro seleccionado
                activity.seleccionarLibro(libro)
            }
        }
        holder.btnVerDetalles.setOnClickListener(alTocar)
        holder.itemView.setOnClickListener(alTocar)
    }

    override fun getItemCount(): Int {
        return listaLibros.size
    }

    fun actualizarLista(nuevaLista: List<dataLibros>) {
        listaLibros = nuevaLista
        notifyDataSetChanged()
    }
}
