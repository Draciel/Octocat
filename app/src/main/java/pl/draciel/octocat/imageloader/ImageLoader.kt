package pl.draciel.octocat.imageloader

import android.net.Uri
import android.util.Size
import android.widget.ImageView

interface ImageLoader {
    fun clear(imageView: ImageView)
    fun loadImage(uri: Uri, size: Size? = null): LoadRequest
    fun loadImage(string: String, size: Size? = null): LoadRequest
}
