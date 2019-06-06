package pl.draciel.octocat.imageloader

import android.widget.ImageView
import com.bumptech.glide.RequestBuilder

class SimpleLoadRequest<T>(private val requestBuilder: RequestBuilder<T>) : LoadRequest {
    override fun into(imageView: ImageView) {
        requestBuilder.into(imageView)
    }
}
