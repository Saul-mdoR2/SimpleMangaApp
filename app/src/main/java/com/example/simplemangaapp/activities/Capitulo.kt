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
    private var paginaActual: Page? = null
    private var capitulo: Chapter? = null
    private var url = ""
    private var titlesChapter: ArrayList<String>? = null
    private var adapterChapters: ArrayAdapter<String>? = null


    private lateinit var model: ActivityCapituloBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ActivityCapituloBinding.inflate(layoutInflater)
        setContentView(model.root)
        network = Network(this)
        capitulo = intent.getSerializableExtra(DetalleManga.CAPITULO) as Chapter
        initToolbar(capitulo?.titleChapter!!)
        url = "http://www.mangatown.com${capitulo!!.linkChapter}"
        changeImage(url)
    }

    private fun changeImage(url: String) {
        val jspoon: Jspoon = Jspoon.create()
        val htmlAdapter: HtmlAdapter<Page> = jspoon.adapter(Page::class.java)
        network!!.httpRequest(
            url,
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {
                    paginaActual = htmlAdapter.fromHtml(response)
                    val enlaceImagen = "http:${paginaActual!!.rutaImagenActual}"
                    Picasso.get().load(enlaceImagen).into(model.ivPagina)
                    val totalPages = paginaActual!!.total!!.count() - 1
                    model.tvPaginaActual.text = resources.getString(
                        R.string.CurrentPage,
                        paginaActual!!.currentPage,
                        totalPages.toString()
                    )
                    initSpinners(paginaActual!!)
                }
            })
    }

    fun initSpinners(pagina: Page) {
        val numPages = ArrayList<String>()
        for (page in pagina.total!!) {
            numPages.add(page.numPage!!)
        }
        numPages.removeAt(numPages.count() - 1)
        val adapterPages = ArrayAdapter(applicationContext, R.layout.spinner_layout, numPages)
        model.listPages.adapter = adapterPages

        if (titlesChapter == null) {
            getTitles(DetalleManga.lista)
        }

        // CAMBIAR VALUE DEL SPINNER DEPENDIENDO DEL CAPITULO Y DE LA PAGINA ACTUAL
        val posicionPage = pagina.currentPage!!.toInt() - 1
        model.listPages.post { model.listPages.setSelection(posicionPage) }

        val posicionChapter: Int = adapterChapters!!.getPosition(capitulo!!.titleChapter)
        model.listChapters.post { model.listChapters.setSelection(posicionChapter) }


        if (model.listPages.onItemSelectedListener == null) {
            model.listPages.onItemSelectedListener = this
        }

        if (model.listChapters.onItemSelectedListener == null) {
            model.listChapters.onItemSelectedListener = this
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            model.listPages -> {
                val numeroSeleccionado = parent.getItemAtPosition(position)
                val seleccionado = paginaActual?.total!!.first { totalPages ->
                    totalPages.numPage == numeroSeleccionado
                }

                if (paginaActual!!.currentPage != seleccionado.numPage) {
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            url = if (paginaActual?.paginaAnterior!!.contains("html")) {
                "http://www.mangatown.com${capitulo!!.linkChapter + paginaActual?.paginaAnterior}"
            } else {
                "http://www.mangatown.com${paginaActual?.paginaAnterior}"
            }
            changeImage(url)
            return true
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            url =
                "http://www.mangatown.com${capitulo!!.linkChapter + paginaActual?.paginaSiguiente}"

            if (paginaActual?.paginaSiguiente!!.contains("featured")) {
                Toast.makeText(this, "The chapter is over", Toast.LENGTH_SHORT).show()
                return true
            }

            changeImage(url)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun getTitles(capitulos: ArrayList<Chapter>) {
        titlesChapter = ArrayList()
        for (chapter in capitulos) {
            titlesChapter!!.add(chapter.titleChapter!!)
        }
        adapterChapters =
            ArrayAdapter(applicationContext, R.layout.spinner_layout, titlesChapter!!)
        model.listChapters.adapter = adapterChapters
    }


}