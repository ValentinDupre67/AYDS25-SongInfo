package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector
import ayds.songinfo.moredetails.fulllogic.view.HomeView

object HomePresenterInjector {
    fun init(homePresenter: HomeView) {
        HomeModelInjector.initHomeModel(homePresenter)
    }
}