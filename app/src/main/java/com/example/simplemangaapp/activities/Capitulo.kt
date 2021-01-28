package com.example.simplemangaapp.activities

import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.Toast
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
    private var ivPagina: ImageView? = null
    private var network:Network? = null
    private var pagina:Page? = null
    private var capitulo:Chapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capitulo)

        network = Network(this)

        capitulo = intent.getSerializableExtra(DetalleManga.CAPITULO) as Chapter

        initToolbar(capitulo!!.titleChapter!!)
        ivPagina = findViewById(R.id.ivPagina)

        network!!.httpRequest(
            "http://www.mangatown.com${capitulo!!.linkChapter}",
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {

                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<Page> =
                        jspoon.adapter(Page::class.java)
                    pagina = htmlAdapter.fromHtml(response)

                    val enlacePagina = "http:${pagina!!.paginaActual}"
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


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Toast.makeText(this, "Bajó volumen", Toast.LENGTH_SHORT).show()
            return true
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            network!!.httpRequest(
                "http://www.mangatown.com${capitulo!!.linkChapter+pagina?.paginaSiguiente}",
                applicationContext,
                object : HttpResponse {
                    override fun httpRespuestaExitosa(response: String) {

                        val jspoon: Jspoon = Jspoon.create()
                        val htmlAdapter: HtmlAdapter<Page> =
                            jspoon.adapter(Page::class.java)
                        pagina = htmlAdapter.fromHtml(response)

                        val enlacePagina = "http:${pagina!!.paginaActual}"
                        Picasso.get().load(enlacePagina).into(ivPagina)
                    }
                })


            return true
        }

        return super.onKeyDown(keyCode, event)
    }


}