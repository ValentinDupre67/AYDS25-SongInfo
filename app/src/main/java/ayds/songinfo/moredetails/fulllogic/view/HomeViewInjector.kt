package ayds.songinfo.moredetails.fulllogic.view

import android.util.Log
import ayds.songinfo.moredetails.fulllogic.presenter.HomePresenterInjector

object HomeViewInjector {
    fun init(homeView: HomeView) {
        Log.d("hola", "HomeViewInjector")
       HomePresenterInjector.init(homeView)
    }
}