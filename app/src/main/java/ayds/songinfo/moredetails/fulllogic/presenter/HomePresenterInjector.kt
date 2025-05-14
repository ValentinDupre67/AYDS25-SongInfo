package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector

object HomePresenterInjector {
    fun init(homePresenter: HomePresenter) {
        HomeModelInjector.initHomeModel(homePresenter)
    }
}