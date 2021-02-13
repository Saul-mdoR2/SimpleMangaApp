package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector
import java.io.Serializable

class Manga : Serializable {
    @Selector(value = ".manga_cover>img", attr = "src")
    var image: String? = null

    @Selector(".title>a")
    var title: String? = null

    @Selector(".new_chapter>a")
    var chapter: String? = null

    @Selector(value = ".title>a", attr = "href")
    var enlaceDetalle: String? = null
}