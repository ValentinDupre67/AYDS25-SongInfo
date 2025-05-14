package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.model.HomeModel
import ayds.songinfo.moredetails.fulllogic.model.HomeModelInjector

interface HomePresenter{
    val uiEventObservable: Observable<HomeUiState> //pre tiene que ser Observable<HomeUiState>
    val uiState: HomeUiState // pre
}

class HomePresenterImpl(): HomePresenter {
    private val onActionSubject = Subject<HomeUiState>() //pre

    private lateinit var homeModel: HomeModel

    override val uiEventObservable: Observable<HomeUiState> = onActionSubject
    override var uiState: HomeUiState = HomeUiState("hola") //por queeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee

    fun




}
