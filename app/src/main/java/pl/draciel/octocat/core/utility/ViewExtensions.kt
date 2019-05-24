package pl.draciel.octocat.core.utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

fun EditText.observeOnTextChanged(): Observable<CharSequence> {
    return Observable.create { emitter ->
        try {
            val textWatcher = object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        emitter.onNext(s)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            }
            addTextChangedListener(textWatcher)
            emitter.setDisposable(Disposables.fromAction { removeTextChangedListener(textWatcher) })
        } catch (ex: NullPointerException) {
            throw ex
        } catch (ex: Exception) {
            emitter.onError(ex)
        }
    }
}
