package pl.draciel.octocat.core.utility

import android.content.Context
import android.content.Intent
import android.net.Uri

object NavigationUtility {

    @JvmStatic
    fun openMailClient(email: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_EMAIL, email)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    @JvmStatic
    fun openWebBrowser(url: String, context: Context) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}
