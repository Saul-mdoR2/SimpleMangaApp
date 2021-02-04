package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector

class Page {
    @Selector(value = "#viewer>a>img", attr = "src")
    var rutaImagenActual: String? = null

    @Selector(value = ".prew_page", attr = "href")
    var paginaAnterior: String? = null

    @Selector(value = ".next_page", attr = "href")
    var paginaSiguiente: String? = null

    @Selector(".go_page>.page_select>select>option[selected]")
    var currentPage: String? = null

    @Selector(".go_page>.page_select>select>option")
    var total: ArrayList<TotalPages>? = null

}
