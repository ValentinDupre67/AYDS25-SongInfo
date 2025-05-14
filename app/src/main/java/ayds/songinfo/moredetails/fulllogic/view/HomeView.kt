package ayds.songinfo.moredetails.fulllogic.view

import ayds.observer.Observable
import ayds.songinfo.home.view.HomeUiEvent
import ayds.songinfo.home.view.HomeUiState

interface HomeView{
    val uiEventObservable: Observable<HomeUiEvent>
    val uiState: HomeUiState
}

class HomeViewImpl():HomeView {
    override val uiEventObservable: Observable<HomeUiEvent>
        get() = TODO("Not yet implemented")
    override val uiState: HomeUiState
        get() = TODO("Not yet implemented")

}