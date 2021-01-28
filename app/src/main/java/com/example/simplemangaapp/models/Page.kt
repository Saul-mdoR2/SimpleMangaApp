package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector

class Page {
    @Selector(value = "#viewer>a>img", attr = "src")
    var pagina:String? = null
}