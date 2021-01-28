package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector

class Page {
    @Selector(value = "#viewer>a>img", attr = "src")
    var paginaActual:String? = null

    @Selector(value = ".prew_page",attr = "href")
    var paginaAnterior:String? = null

    @Selector(value = ".next_page", attr = "href")
    var paginaSiguiente:String? = null

}