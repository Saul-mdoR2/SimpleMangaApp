package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector

class MangaPrincipal {
    @Selector(".manga_pic_list>li")
    var listMangas:ArrayList<Manga>? = null
}