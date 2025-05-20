package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector

object HomePresenterInjector {

    private lateinit var homePresenter: HomePresenter
    fun getPresenter(): HomePresenter = homePresenter
    fun init(){
        HomeModelInjector.init()
    }
}