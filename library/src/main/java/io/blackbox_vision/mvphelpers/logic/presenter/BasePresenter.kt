package io.blackbox_vision.mvphelpers.logic.presenter

import io.blackbox_vision.mvphelpers.logic.view.BaseView


abstract class BasePresenter<T : BaseView> {
    var view: T? = null
        private set

    protected val isViewAttached: Boolean
        get() = this.view != null

    abstract fun onViewAttached(view: T?)

    abstract fun onViewDetached(view: T?)

    fun attachView(view: T) {
        this.view = view
        onViewAttached(view)
    }

    fun detachView() {
        onViewDetached(this.view)
        this.view = null
    }
}
