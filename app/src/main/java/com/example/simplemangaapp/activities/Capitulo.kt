package com.example.simplemangaapp.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.simplemangaapp.R
import com.example.simplemangaapp.models.Chapter
import com.example.simplemangaapp.models.Page
import com.example.simplemangaapp.utilities.HttpResponse
import com.example.simplemangaapp.utilities.Network
import com.squareup.picasso.Picasso
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon

class Capitulo : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var ivPagina:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capitulo)

        val network = Network(this)

        val capitulo = intent.getSerializableExtra(DetalleManga.CAPITULO) as Chapter

        initToolbar(capitulo.titleChapter!!)
        ivPagina = findViewById(R.id.ivPagina)

        network.httpRequest(
            "http://www.mangatown.com${capitulo.linkChapter}",
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {

                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<Page> =
                        jspoon.adapter(Page::class.java)
                    val pagina: Page = htmlAdapter.fromHtml(response)

                    val enlacePagina = "http:${pagina.pagina}"
                    Picasso.get().load(enlacePagina).into(ivPagina)
                }
            })

    }

    @Suppress("DEPRECATION")
    fun initToolbar(titleManga: String) {
        toolbar = findViewById(R.id.toolbarCapitulo)
        toolbar?.setTitleTextColor(resources.getColor(R.color.white))
        toolbar?.title = titleManga
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener {
            finish()
        }
        val actionbar = supportActionBar

        actionbar?.setDisplayHomeAsUpEnabled(true)
    }
}