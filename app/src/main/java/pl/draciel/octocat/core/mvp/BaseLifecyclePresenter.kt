package pl.draciel.octocat.core.mvp

abstract class BaseLifecyclePresenter<T : BaseView> : LifecyclePresenter<T> {

    protected var view: T? = null

    override fun attachView(view: T) {
        this.view = view
    }

    override fun destroy() {
        detachView()
    }

    override fun detachView() {
        this.view = null
    }

}
