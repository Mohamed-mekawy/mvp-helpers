package io.blackbox_vision.helpers.logic.presenter

import android.os.Bundle

import io.blackbox_vision.helpers.logic.view.DetailsView
import io.blackbox_vision.helpers.logic.interactor.DetailsInteractor
import io.blackbox_vision.mvphelpers.logic.listener.OnErrorListener
import io.blackbox_vision.mvphelpers.logic.listener.OnSuccessListener
import io.blackbox_vision.mvphelpers.logic.presenter.BasePresenter

class DetailsPresenter : BasePresenter<DetailsView>(), OnSuccessListener<Bundle>, OnErrorListener<String> {
    private var interactor: DetailsInteractor? = null

    override fun onViewAttached(view: DetailsView?) {
        interactor = DetailsInteractor.newInstance()
        //reload app state
    }

    override fun onViewDetached(view: DetailsView?) {
        interactor = null
        //save app state
    }

    fun findRequiredInformation(id: String) {
        if (isViewAttached) {
            interactor!!.retrieveDetailsFromService(id, this, this)
        }
    }

    override fun onSuccess(data: Bundle) {
        if (isViewAttached) {
            view!!.onInfoReceived(data)
        }
    }

    override fun onError(error: String) {
        if (isViewAttached) {
            view!!.onInfoError(error)
        }
    }

    companion object {
        private val detailsPresenter: DetailsPresenter = DetailsPresenter()

        fun newInstance(): DetailsPresenter {
            return detailsPresenter
        }
    }
}
