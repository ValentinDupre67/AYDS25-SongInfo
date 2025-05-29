package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.moredetails.fulllogic.domain.Repository

interface Presenter {
    val infoObservable: Observable<ArtistBiography>
    fun getArtistInfoFromRepository(artistName: String)
}

internal class PresenterImpl(
    private val repositoy: Repository
) : Presenter {

    override val infoObservable = Subject<ArtistBiography>()

    override fun getArtistInfoFromRepository(artistName: String) {
        val info = repositoy.getArtistInfoFromRepository(artistName)
        infoObservable.notify(info)
    }
}