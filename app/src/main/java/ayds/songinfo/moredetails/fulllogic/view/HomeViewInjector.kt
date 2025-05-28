package ayds.songinfo.moredetails.fulllogic.view

import android.util.Log
import ayds.songinfo.moredetails.fulllogic.presenter.HomePresenterInjector

object HomeViewInjector {

    //TODO Estudiar esto como funca
    val homeViewResolver : HomeViewResolver = HomeViewResolverImpl()
    fun init(homeView: HomeView) {
        Log.d("hola", "HomeViewInjector")
       HomePresenterInjector.init(homeView)
    }
}