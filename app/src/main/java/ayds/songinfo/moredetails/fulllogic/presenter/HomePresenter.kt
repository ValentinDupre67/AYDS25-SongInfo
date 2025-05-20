package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.observer.Observable
import ayds.observer.Observer
import ayds.observer.Subject
import retrofit2.Response

interface HomePresenter{
    val songObservable: Observable<Response<String>>
}

internal class HomePresenterImpl(): HomePresenter{

    override val songObservable = Subject<Response<String>>()



}