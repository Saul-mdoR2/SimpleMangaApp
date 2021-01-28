package com.example.simplemangaapp.recyclerViewPrincipal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemangaapp.models.Manga
import com.example.simplemangaapp.R
import com.squareup.picasso.Picasso

class AdapterCustom(items: ArrayList<Manga>, var listener: ClickListener) :
    RecyclerView.Adapter<AdapterCustom.ViewHolder>() {

    private var items: ArrayList<Manga>? = null
    private var viewHolder: ViewHolder? = null

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.template_manga, parent, false)
        viewHolder =
            ViewHolder(
                vista, listener
            )
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)

        holder.nombre?.text = item?.title
        holder.capitulo?.text = item?.chapter?.replace(item.title!!, "Chapter")
        Picasso.get().load(item?.image).into(holder.imagen)


    }

    class ViewHolder(vista: View, listener: ClickListener) : RecyclerView.ViewHolder(vista),
        View.OnClickListener {
        private var view: View = vista
        var nombre: TextView? = null
        var imagen: ImageView? = null
        var capitulo: TextView? = null

        private var clickListener: ClickListener? = null

        init {
            nombre = view.findViewById(R.id.tvMangaName) as TextView
            imagen = view.findViewById(R.id.ivMangaTemplateRV) as ImageView
            capitulo = view.findViewById(R.id.tvChapter) as TextView

            clickListener = listener

            vista.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onClick(v!!, adapterPosition)
        }
    }

}