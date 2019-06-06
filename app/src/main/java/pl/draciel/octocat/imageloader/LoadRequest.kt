package pl.draciel.octocat.imageloader

import android.widget.ImageView

interface LoadRequest {
    fun into(imageView: ImageView)

}
