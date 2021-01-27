package com.example.simplemangaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon

@Suppress("DEPRECATION")
class DetalleManga : AppCompatActivity() {

    private var toolbar: Toolbar? = null

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
                    val tv = findViewById<TextView>(R.id.tvaaaa)
                    val img = findViewById<ImageView>(R.id.ivMangaDetalle)
                    val jspoon:Jspoon = Jspoon.create()
                    val htmlAdapter:HtmlAdapter<MangaDetails> = jspoon.adapter(MangaDetails::class.java)
                    val mangaDetails:MangaDetails = htmlAdapter.fromHtml(response)

                    tv.text = mangaDetails.title
                    Picasso.get().load(mangaDetails.image).into(img)
                }
            })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }

   fun initToolbar(tituloManga:String){
       toolbar = findViewById(R.id.toolbarDetalle)
       toolbar?.setTitleTextColor(resources.getColor(R.color.white))
       toolbar?.title = tituloManga
       setSupportActionBar(toolbar)
       val actionbar = supportActionBar
       actionbar?.setDisplayHomeAsUpEnabled(true)
    }
}

