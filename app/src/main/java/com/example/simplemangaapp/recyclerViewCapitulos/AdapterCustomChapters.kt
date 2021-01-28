package com.example.simplemangaapp.recyclerViewCapitulos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemangaapp.Chapter
import com.example.simplemangaapp.R

class AdapterCustomChapters(items: ArrayList<Chapter>, var listenerChapter: ClickListenerChapter) :
    RecyclerView.Adapter<AdapterCustomChapters.ViewHolder>() {

    private var items: ArrayList<Chapter>? = null
    private var viewHolder: ViewHolder? = null

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.template_chapters, parent, false)
        viewHolder =
            ViewHolder(
                vista, listenerChapter
            )
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.tvTitleChapter?.text = item?.titleChapter
        holder.tvReleaseDate?.text = item?.date
    }

    class ViewHolder(vista: View, listener: ClickListenerChapter) : RecyclerView.ViewHolder(vista),
        View.OnClickListener {
        private var view: View = vista
         var tvTitleChapter:TextView? = null
        var tvReleaseDate:TextView? = null


        private var clickListener: ClickListenerChapter? = null

        init {

            tvTitleChapter = view.findViewById(R.id.tvTitleChapter) as TextView
           tvReleaseDate = view.findViewById(R.id.tvReleaseDate) as TextView
            clickListener = listener

            vista.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onClick(v!!, adapterPosition)
        }
    }
}