package ayds.songinfo.home.view

import ayds.songinfo.home.controller.HomeControllerInjector
import ayds.songinfo.home.model.HomeModelInjector

object HomeViewInjector {
    private val releaseDateHelper: ReleaseDateHelper = ReleaseDateImpl()

    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(releaseDateHelper)

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}