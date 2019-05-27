package pl.draciel.octocat.app.ui.search

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import pl.draciel.octocat.concurrent.SchedulerSupportExtension

internal class RxSearchQueryListener : SearchView.OnQueryTextListener {

    private val onTextChanged: Subject<String> = PublishSubject.create()
    private val onTextSubmit: Subject<String> = PublishSubject.create()

    override fun onQueryTextSubmit(query: String?): Boolean {
        try {
            if (query != null) {
                onTextSubmit.onNext(query)
            }
        } catch (npe: NullPointerException) {
            throw npe
        } catch (ex: Exception) {
            onTextSubmit.onError(ex)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        try {
            if (newText != null) {
                onTextChanged.onNext(newText)
            }
        } catch (npe: NullPointerException) {
            throw npe
        } catch (ex: Exception) {
            onTextChanged.onError(ex)
        }
        return false
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupportExtension.MAIN_THREAD_SCHEDULER)
    fun observeOnTextChanged(): Observable<String> = onTextChanged.distinctUntilChanged()

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupportExtension.MAIN_THREAD_SCHEDULER)
    fun observeOnTextSubmit(): Observable<String> = onTextSubmit.distinctUntilChanged()
}
