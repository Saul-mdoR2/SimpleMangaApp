package com.example.simplemangaapp.models

import com.example.simplemangaapp.models.Manga
import pl.droidsonroids.jspoon.annotation.Selector

class MangaPrincipal {
    @Selector(".manga_pic_list>li")
    var listMangas:ArrayList<Manga>? = null
}