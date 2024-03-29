package pl.draciel.octocat.core.mvp

abstract class BaseLifecyclePresenter<T : BaseView> : LifecyclePresenter<T> {

    protected var view: T? = null

    override fun attachView(view: T) {
        this.view = view
    }

    override fun destroy() {
        if (isViewAttached()) {
            detachView()
        }
    }

    override fun detachView() {
        this.view = null
    }

    override fun isViewAttached(): Boolean = this.view != null

}
