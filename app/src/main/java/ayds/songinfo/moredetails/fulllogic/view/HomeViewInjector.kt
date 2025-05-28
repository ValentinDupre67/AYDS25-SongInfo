package ayds.songinfo.moredetails.fulllogic.view

import ayds.songinfo.moredetails.fulllogic.presenter.HomePresenterInjector

object HomeViewInjector {

    //TODO Estudiar esto como funca
    val homeViewResolver : HomeViewResolver = HomeViewResolverImpl()
    fun init(homeView: HomeView) {
       HomePresenterInjector.init(homeView)
    }
}