package ayds.songinfo.moredetails.fulllogic.presenter

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.domain.ArticleRepository

interface HomePresenter{
    val songObservable: Observable<ArtistBiography>
    fun getArtistInfo(artistName: String)
}

internal class HomePresenterImpl(
    private var repository: ArticleRepository) : HomePresenter{

    override val songObservable = Subject<ArtistBiography>()

    override fun getArtistInfo(artistName: String) {
        repository.getArtistInfo(artistName).let {
            songObservable.notify(it)
        }

    }

}