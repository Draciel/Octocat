package pl.draciel.octocat.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Size
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import pl.draciel.octocat.core.di.scopes.ApplicationContext

class GlideImageLoader(@ApplicationContext private val appContext: Context) : ImageLoader {

    private val requestBuilder: RequestBuilder<Drawable>
    private val manager: RequestManager

    init {
        val options = RequestOptions()
                .encodeQuality(90)
                .dontAnimate()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        manager = Glide.with(appContext)

        requestBuilder = manager
                .applyDefaultRequestOptions(options)
                .asDrawable()
    }

    override fun loadImage(uri: Uri, size: Size?): LoadRequest {
        val request = requestBuilder.clone()
                .load(uri)
                .let { r -> size?.let { r.override(it.width, it.height) } ?: r }
        return SimpleLoadRequest(request)
    }

    override fun loadImage(string: String, size: Size?): LoadRequest {
        val request = requestBuilder.clone()
                .load(string)
                .let { r -> size?.let { r.override(it.width, it.height) } ?: r }
        return SimpleLoadRequest(request)
    }

    override fun clear(imageView: ImageView) {
        manager.clear(imageView)
    }
}
