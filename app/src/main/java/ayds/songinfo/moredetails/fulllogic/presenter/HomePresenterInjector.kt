package ayds.songinfo.moredetails.fulllogic.presenter

import android.util.Log
import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector
import ayds.songinfo.moredetails.fulllogic.view.HomeView

object HomePresenterInjector {

    private lateinit var homePresenter: HomePresenter
    fun getPresenter(): HomePresenter = homePresenter
    fun init(context: HomeView) {
        HomeModelInjector.init(context)

        Log.d("hola", "antes homeModel")
        val homeModel = HomeModelInjector.getHomeModel()
        Log.d("hola", "despues homeModel")
        homePresenter = HomePresenterImpl(homeModel)

    }
}