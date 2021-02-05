package com.example.simplemangaapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplemangaapp.R
import com.example.simplemangaapp.databinding.ActivityDetalleMangaBinding
import com.example.simplemangaapp.models.Chapter
import com.example.simplemangaapp.models.Manga
import com.example.simplemangaapp.models.MangaDetails
import com.example.simplemangaapp.recyclerViewCapitulos.AdapterCustomChapters
import com.example.simplemangaapp.recyclerViewCapitulos.ClickListenerChapter
import com.example.simplemangaapp.utilities.HttpResponse
import com.example.simplemangaapp.utilities.Network
import com.squareup.picasso.Picasso
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon

@Suppress("DEPRECATION")
class DetalleManga : AppCompatActivity() {

    private var mangaActual: Manga? = null

    companion object {
        const val CAPITULO = "com.example.simplemangaapp.activities.DetalleManga"
        var lista = ArrayList<Chapter>()
    }


    private lateinit var model: ActivityDetalleMangaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ActivityDetalleMangaBinding.inflate(layoutInflater)
        setContentView(model.root)

        mangaActual = intent.getSerializableExtra(MainActivity.TAG) as Manga

        initToolbar(mangaActual?.title!!)

        val network = Network(this)


        network.httpRequest(
            "http://www.mangatown.com${mangaActual!!.enlaceDetalle}",
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {

                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<MangaDetails> =
                        jspoon.adapter(MangaDetails::class.java)
                    val mangaDetails: MangaDetails = htmlAdapter.fromHtml(response)
                    asignarValores(mangaDetails)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun initToolbar(titleManga: String) {
        model.toolbarDetalle.setTitleTextColor(resources.getColor(R.color.white))
        model.toolbarDetalle.title = titleManga
        setSupportActionBar(model.toolbarDetalle)
        model.toolbarDetalle.setNavigationOnClickListener {
            finish()
        }
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }


    fun asignarValores(mangaDetails: MangaDetails) {
        model.tvGenre.text = mangaDetails.genre?.replace(":", ": ")!!.replace(",", ", ")
        model.tvAuthor.text = mangaDetails.author?.replace(":", ": ")
        model.tvStatus.text = mangaDetails.status?.replace(":", ": ")!!.replace("Ongoing", "")
        model.tvArtist.text = mangaDetails.artist?.replace(":", ": ")
        model.tvType.text =
            resources.getString(R.string.type, mangaDetails.type?.replace(":", ": "))
        Picasso.get().load(mangaDetails.image).placeholder(R.drawable.manga_cover)
            .into(model.ivManga)
        model.tvSummary.text = mangaDetails.summary?.replace("HIDE", "")

        if (mangaDetails.listaCapitulos != null) {
            initAdaptador(mangaDetails.listaCapitulos!!)
            lista = mangaDetails.listaCapitulos!!
        } else {
            Toast.makeText(
                this,
                "${mangaActual?.title} it's not available in MangaTown",
                Toast.LENGTH_LONG
            )
                .show() // ALGUNOS MANGAS TIENEN LICENCIA, COMO EL DE Boku no Hero Academia Y SE CORTA LA EJECUCION DE LA APP
            mangaDetails.listaCapitulos = ArrayList()
        }
    }

    private fun initAdaptador(lista: ArrayList<Chapter>) {
        val linearLayoutManager = GridLayoutManager(this, 3)
        model.rvChapters.setHasFixedSize(true)
        model.rvChapters.layoutManager = linearLayoutManager

        val adapter = AdapterCustomChapters(lista, object : ClickListenerChapter {
            override fun onClick(vista: View, index: Int) {
                val intent = Intent(applicationContext, Capitulo::class.java)
                val capitulo = lista[index]
                intent.putExtra(CAPITULO, capitulo)
                startActivity(intent)
            }
        })
        model.rvChapters.adapter = adapter
    }
}

