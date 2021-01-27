package com.example.simplemangaapp

import pl.droidsonroids.jspoon.annotation.Selector
import java.io.Serializable

class Chapter:Serializable {
    @Selector("a")
    var titleChapter:String? = null
    @Selector(".time")
    var date:String? = null
    @Selector(value = "a", attr = "href")
    var linkChapter:String? = null
}