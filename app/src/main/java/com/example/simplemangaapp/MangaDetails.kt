package com.example.simplemangaapp

import pl.droidsonroids.jspoon.annotation.Selector

class MangaDetails {
    @Selector(value = ".detail_content>div>img", attr = "src")
    var image:String? = null
    @Selector(".title-top")
    var title:String? = null
}