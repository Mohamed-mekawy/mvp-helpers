package io.blackbox_vision.mvphelpers.logic.presenter;

import android.support.annotation.NonNull;

import io.blackbox_vision.mvphelpers.logic.view.BaseView;


public abstract class BasePresenter<V extends BaseView> {
    private V view;

    protected abstract void onViewAttached(@NonNull V view);

    protected abstract void onViewDetached();

    public boolean isViewAttached() {
        return this.view != null;
    }

    public void attachView(@NonNull V view) {
        this.view = view;
        onViewAttached(view);
    }

    public void detachView() {
        this.view = null;
        onViewDetached();
    }

    public V getView() {
        return view;
    }
}
