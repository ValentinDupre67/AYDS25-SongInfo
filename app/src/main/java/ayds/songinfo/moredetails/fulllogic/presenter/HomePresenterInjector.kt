package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector
import ayds.songinfo.moredetails.fulllogic.view.HomePresenter

object HomePresenterInjector {
    fun init(homePresenter: HomePresenter) {
        HomeModelInjector.initHomeModel(homePresenter)
    }
}