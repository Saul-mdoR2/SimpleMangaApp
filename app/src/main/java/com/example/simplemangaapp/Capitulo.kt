package com.example.simplemangaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Capitulo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capitulo)

        val capitulo = intent.getSerializableExtra(DetalleManga.CAPITULO) as Chapter
        capitulo.titleChapter
    }
}