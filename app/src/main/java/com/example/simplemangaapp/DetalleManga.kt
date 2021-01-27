package com.example.simplemangaapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemangaapp.recyclerViewCapitulos.AdapterCustomChapters
import com.example.simplemangaapp.recyclerViewCapitulos.ClickListenerChapter
import com.example.simplemangaapp.recyclerViewPrincipal.AdapterCustom
import com.squareup.picasso.Picasso
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon

@Suppress("DEPRECATION")
class DetalleManga : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var tvGenre: TextView? = null
    private var tvAuthor: TextView? = null
    private var tvStatus: TextView? = null
    private var tvArtist: TextView? = null
    private var tvType: TextView? = null
    private var ivManga: ImageView? = null
    private var tvSummary: TextView? = null
    private var rvChapters:RecyclerView? = null
    var listChapters:ArrayList<Chapter>? = null

    companion object{
        val CAPITULO = "com.example.simplemangaapp.DetalleManga"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_manga)

        val mangaActual = intent.getSerializableExtra(MainActivity.TAG) as Manga

        initToolbar(mangaActual.title!!)

        listChapters = ArrayList()

        val network = Network(this)


        network.httpRequest(
            "http://www.mangatown.com${mangaActual.enlaceDetalle}",
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {

                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<MangaDetails> =
                        jspoon.adapter(MangaDetails::class.java)
                    val mangaDetails: MangaDetails = htmlAdapter.fromHtml(response)

                    initGraphicElements()
                    asignarValores(mangaDetails)
                }
            })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }


    fun initToolbar(titleManga: String) {
        toolbar = findViewById(R.id.toolbarDetalle)
        toolbar?.setTitleTextColor(resources.getColor(R.color.white))
        toolbar?.title = titleManga
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            finish()
        }
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    fun initGraphicElements() {
        tvGenre = findViewById(R.id.tvGenre)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvStatus = findViewById(R.id.tvStatus)
        tvArtist = findViewById(R.id.tvArtist)
        tvType = findViewById(R.id.tvType)
        ivManga = findViewById(R.id.ivManga)
        tvSummary = findViewById(R.id.tvSummary)

        rvChapters = findViewById(R.id.rvChapters)
    }

    fun asignarValores(mangaDetails: MangaDetails) {
        tvGenre?.text = mangaDetails.genre?.replace(":", ": ")!!.replace(",", ", ")
        tvAuthor?.text = mangaDetails.author?.replace(":", ": ")
        tvStatus?.text = mangaDetails.status?.replace(":", ": ")!!.replace("Ongoing", "")
        tvArtist?.text = mangaDetails.artist?.replace(":", ": ")
        tvType?.text = resources.getString(R.string.type, mangaDetails.type?.replace(":", ": "))
        Picasso.get().load(mangaDetails.image).into(ivManga)
        tvSummary?.text = mangaDetails.summary?.replace("HIDE", "")

        listChapters = mangaDetails.listaCapitulos

        initAdaptador(listChapters!!)

    }

    fun initAdaptador(lista:ArrayList<Chapter>){
        val linearLayoutManager = LinearLayoutManager(this)
        rvChapters?.setHasFixedSize(true)
        rvChapters?.layoutManager = linearLayoutManager

        val adapter = AdapterCustomChapters(lista, object :ClickListenerChapter{
            override fun onClick(vista: View, index: Int) {
                val intent = Intent(applicationContext, Capitulo::class.java)
                val capitulo = lista[index]
                intent.putExtra(CAPITULO,capitulo)
                startActivity(intent)
            }
        })
        rvChapters?.adapter = adapter
    }
}

