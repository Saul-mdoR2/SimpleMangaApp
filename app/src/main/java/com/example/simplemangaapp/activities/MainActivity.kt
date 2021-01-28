package com.example.simplemangaapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemangaapp.R
import com.example.simplemangaapp.models.Manga
import com.example.simplemangaapp.models.MangaPrincipal
import com.example.simplemangaapp.recyclerViewPrincipal.AdapterCustom
import com.example.simplemangaapp.recyclerViewPrincipal.ClickListener
import com.example.simplemangaapp.utilities.HttpResponse
import com.example.simplemangaapp.utilities.Network
import pl.droidsonroids.jspoon.HtmlAdapter
import pl.droidsonroids.jspoon.Jspoon


class MainActivity : AppCompatActivity() {

    private var rvlist: RecyclerView? = null
    private var adaptador: AdapterCustom? = null
    private var toolbar: Toolbar? = null
    private var listaMangas:ArrayList<Manga>? = null

    companion object {
        const val TAG = "com.example.simplemangaapp.activities.MainActivity"
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvlist = findViewById(R.id.rvLista)

        toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar?.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)

        val gridLayoutManager = GridLayoutManager(this, 3)
        rvlist?.setHasFixedSize(true)
        rvlist?.layoutManager = gridLayoutManager


        listaMangas = ArrayList()

        val network = Network(this)

        network.httpRequest(
            "http://www.mangatown.com/latest/",
            applicationContext,
            object : HttpResponse {
                override fun httpRespuestaExitosa(response: String) {
                   val jspoon:Jspoon = Jspoon.create()
                   val htmlAdapter:HtmlAdapter<MangaPrincipal> = jspoon.adapter(
                       MangaPrincipal::class.java)
                    val mangaPrincipal: MangaPrincipal = htmlAdapter.fromHtml(response)
                    for (manga in mangaPrincipal.listMangas!!){
                        listaMangas!!.add(manga)
                    }
                    initAdapter()
                }
            })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun initAdapter(){
        adaptador =
            AdapterCustom(
                listaMangas!!, object : ClickListener {
                    override fun onClick(vista: View, index: Int) {
                        val intent = Intent(applicationContext, DetalleManga::class.java)
                        val mangaActual = listaMangas!![index]
                        intent.putExtra(TAG, mangaActual)
                        startActivity(intent)
                    }
                }
            )

        rvlist?.adapter = adaptador
    }


}