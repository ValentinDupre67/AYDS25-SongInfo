package ayds.songinfo.moredetails.fulllogic.presenter

object HomeViewInjector {

    //TODO Estudiar esto como funca
    val homeViewResolver : HomeViewResolver = HomeViewResolverImpl()
    fun init(homeView: HomeView) {
       HomePresenterInjector.init(homeView)
    }
}