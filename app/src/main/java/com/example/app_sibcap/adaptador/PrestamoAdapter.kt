package com.example.app_sibcap.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_sibcap.R
import com.example.app_sibcap.modelo.Prestamo
import com.example.app_sibcap.utils.Formato

class PrestamoAdapter(
    private var listaPrestamos: List<Prestamo>,
    private val alVerDetalle: (Prestamo) -> Unit
) : RecyclerView.Adapter<PrestamoAdapter.PrestamoViewHolder>() {

    class PrestamoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPortada: TextView = itemView.findViewById(R.id.tvItemPortada)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvItemTitulo)
        val tvAutor: TextView = itemView.findViewById(R.id.tvItemAutor)
        val tvFecha: TextView = itemView.findViewById(R.id.tvItemFecha)
        val tvEstado: TextView = itemView.findViewById(R.id.tvItemEstado)
        val tvMulta: TextView = itemView.findViewById(R.id.tvItemMulta)
        val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestamoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestamo, parent, false)
        return PrestamoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrestamoViewHolder, position: Int) {
        val prestamo = listaPrestamos[position]

        holder.tvPortada.text = prestamo.titulo.firstOrNull()?.uppercase() ?: "?"
        holder.tvTitulo.text = prestamo.titulo
        holder.tvAutor.text = prestamo.autor
        holder.tvFecha.text = Formato.fecha(prestamo.fechaDevolucion)

        val esAviso = prestamo.vencido || prestamo.idEstatus == 3
        holder.tvEstado.text = if (esAviso) "Vencido" else "Vigente"

        if (prestamo.multa > 0) {
            holder.tvMulta.text = Formato.mxn(prestamo.multa)
            holder.tvMulta.visibility = View.VISIBLE
        } else {
            holder.tvMulta.visibility = View.GONE
        }

        val alTocar = View.OnClickListener { alVerDetalle(prestamo) }
        holder.btnVerDetalles.setOnClickListener(alTocar)
        holder.itemView.setOnClickListener(alTocar)
    }

    override fun getItemCount(): Int = listaPrestamos.size

    fun actualizarLista(nuevaLista: List<Prestamo>) {
        listaPrestamos = nuevaLista
        notifyDataSetChanged()
    }
}