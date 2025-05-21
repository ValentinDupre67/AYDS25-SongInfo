package ayds.songinfo.moredetails.fulllogic.view

import ayds.songinfo.moredetails.fulllogic.presenter.HomePresenterInjector

object HomeViewInjector {
    fun init(homeView: HomeView) {
       HomePresenterInjector.init(homeView)
    }
}