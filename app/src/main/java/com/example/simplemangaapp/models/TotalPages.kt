package com.example.simplemangaapp.models

import pl.droidsonroids.jspoon.annotation.Selector

class TotalPages {
    @Selector("option")
    var numPage: String? = null

    @Selector(value = "option", attr = "value")
    var linkPage: String? = null
}