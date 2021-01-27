package com.example.simplemangaapp

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_manga)

        val mangaActual = intent.getSerializableExtra(MainActivity.TAG) as Manga

        initToolbar(mangaActual.title!!)

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
    }

    fun asignarValores(mangaDetails: MangaDetails) {
        tvGenre?.text = mangaDetails.genre?.replace(":", ": ")!!.replace(",", ", ")
        tvAuthor?.text = mangaDetails.author?.replace(":", ": ")
        tvStatus?.text = mangaDetails.status?.replace(":", ": ")!!.replace("Ongoing", "")
        tvArtist?.text = mangaDetails.artist?.replace(":", ": ")
        tvType?.text = resources.getString(R.string.type, mangaDetails.type?.replace(":", ": "))
        Picasso.get().load(mangaDetails.image).into(ivManga)
        tvSummary?.text = mangaDetails.summary?.replace("HIDE", "")
    }
}

