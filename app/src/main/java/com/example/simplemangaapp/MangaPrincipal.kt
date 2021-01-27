package com.example.simplemangaapp

import pl.droidsonroids.jspoon.annotation.Selector
import java.lang.NullPointerException

class MangaPrincipal {
    @Selector(".manga_pic_list>li")
    var listMangas:ArrayList<Manga>? = null
}