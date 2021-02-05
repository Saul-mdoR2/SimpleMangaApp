package com.example.simplemangaapp.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

class Capitulo : AppCompatActivity(), AdapterView.OnItemSelectedListener {
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

    private fun changeImage(url: String) {
        network!!.httpRequest(
            url,
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {
                    val jspoon: Jspoon = Jspoon.create()
                    val htmlAdapter: HtmlAdapter<Page> =
                        jspoon.adapter(Page::class.java)
                    pagina = htmlAdapter.fromHtml(response)
                    val enlacePagina = "http:${pagina!!.rutaImagenActual}"
                    Picasso.get().load(enlacePagina).into(model.ivPagina)
                    val totalPages = pagina!!.total!!.count() - 1
                    model.tvPaginaActual.text = resources.getString(
                        R.string.CurrentPage,
                        pagina!!.currentPage?.replace("0", ""),
                        totalPages.toString()
                    )
                    initSpinners(pagina!!)
                }
            })
    }

    fun initSpinners(pagina: Page) {
        val numChapters = ArrayList<String>()
        for (page in pagina.total!!) {
            numChapters.add(page.numPage!!)
        }
        numChapters.removeAt(numChapters.count() - 1)
        val adapterPages =
            ArrayAdapter(applicationContext, R.layout.spinner_layout, numChapters)
        model.listPages.adapter = adapterPages


        val titlesChapter = ArrayList<String>()
        for (chapter in DetalleManga.lista) {
            titlesChapter.add(chapter.titleChapter!!)
        }
        val adapterChapters =
            ArrayAdapter(applicationContext, R.layout.spinner_layout, titlesChapter)
        model.listChapters.adapter = adapterChapters

        // CAMBIAR VALUE DEL SPINNER DEPENDIENDO DEL CAPITULO Y DE LA PAGINA ACTUAL
        val posicionPage = pagina.currentPage!!.toInt() - 1
        model.listPages.post { model.listPages.setSelection(posicionPage) }

        val posicionCapitulo: Int = adapterChapters.getPosition(capitulo!!.titleChapter)
        model.listChapters.post { model.listChapters.setSelection(posicionCapitulo) }


        model.listPages.onItemSelectedListener = this
        model.listChapters.onItemSelectedListener = this
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            model.listPages -> {
                val seleccionado = pagina?.total!!.first { totalPages ->
                    val numeroSeleccionado = parent.getItemAtPosition(position)
                    totalPages.numPage == numeroSeleccionado
                }
                if (pagina!!.currentPage != seleccionado.numPage) {
                    changeImage("http://www.mangatown.com${seleccionado.linkPage!!}")
                }
            }
            model.listChapters -> {
                val seleccionado = DetalleManga.lista.first { chapter ->
                    val capituloSeleccionado = parent.getItemAtPosition(position)
                    chapter.titleChapter == capituloSeleccionado
                }

                if (capitulo!!.titleChapter != seleccionado.titleChapter) {
                    capitulo = seleccionado
                    model.toolbarCapitulo.title = seleccionado.titleChapter
                    changeImage("http://www.mangatown.com${seleccionado.linkChapter}")
                }
            }
        }

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

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            url = if (pagina?.paginaAnterior!!.contains("html")) {
                "http://www.mangatown.com${capitulo!!.linkChapter + pagina?.paginaAnterior}"
            } else {
                "http://www.mangatown.com${pagina?.paginaAnterior}"
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


}