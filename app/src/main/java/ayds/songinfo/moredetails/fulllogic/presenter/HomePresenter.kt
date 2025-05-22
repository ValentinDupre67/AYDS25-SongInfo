package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.model.HomeModel
import ayds.songinfo.moredetails.fulllogic.view.ArtistBiography

interface HomePresenter{
    val songObservable: Observable<ArtistBiography>
    fun getArtistInfo(artistName: String)
}

internal class HomePresenterImpl(
    private val homeModel: HomeModel) : HomePresenter{

    override val songObservable = Subject<ArtistBiography>()

    override fun getArtistInfo(artistName: String) {
        homeModel.getArtistInfo(artistName).let {
            songObservable.notify(it)
        }

    }

}