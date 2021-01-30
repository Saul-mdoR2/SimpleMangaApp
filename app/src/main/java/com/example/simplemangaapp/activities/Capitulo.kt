package com.example.simplemangaapp.activities

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemangaapp.R
import com.example.simplemangaapp.databinding.ActivityCapituloBinding
import com.example.simplemangaapp.models.Chapter
import com.example.simplemangaapp.models.Page
import com.example.simplemangaapp.utilities.HttpResponse
import com.example.simplemangaapp.utilities.Network
import com.squareup.picasso.Picasso
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon

class Capitulo : AppCompatActivity() {
    private var network: Network? = null
    private var pagina: Page? = null
    private var capitulo: Chapter? = null
    private var url = ""


    private lateinit var model: ActivityCapituloBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ActivityCapituloBinding.inflate(layoutInflater)
        setContentView(model.root)

        network = Network(this)

        capitulo = intent.getSerializableExtra(DetalleManga.CAPITULO) as Chapter
        initToolbar(capitulo!!.titleChapter!!)

        url = "http://www.mangatown.com${capitulo!!.linkChapter}"
        changeImage(url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (pagina?.paginaAnterior!!.contains("html")) {
                url = "http://www.mangatown.com${capitulo!!.linkChapter + pagina?.paginaAnterior}"
            } else {
                url = "http://www.mangatown.com${pagina?.paginaAnterior}"
            }
            changeImage(url)
            return true
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            url = "http://www.mangatown.com${capitulo!!.linkChapter + pagina?.paginaSiguiente}"

            if (pagina?.paginaSiguiente!!.contains("featured")) {
                Toast.makeText(this, "The chapter is over", Toast.LENGTH_SHORT).show()
                return true
            }

            changeImage(url)
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    fun changeImage(url: String) {
        network!!.httpRequest(
            url,
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {
                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<Page> =
                        jspoon.adapter(Page::class.java)
                    pagina = htmlAdapter.fromHtml(response)
                    val enlacePagina = "http:${pagina!!.paginaActual}"
                    Picasso.get().load(enlacePagina).into(model.ivPagina)
                }
            })
    }

    @Suppress("DEPRECATION")
    fun initToolbar(titleManga: String) {
        model.toolbarCapitulo.setTitleTextColor(resources.getColor(R.color.white))
        model.toolbarCapitulo.title = titleManga
        setSupportActionBar(model.toolbarCapitulo)
        model.toolbarCapitulo.setNavigationOnClickListener {
            finish()
        }
        val actionbar = supportActionBar

        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

}