package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.songinfo.moredetails.fulllogic.domain.Repository

interface Presenter{
    val artistBiographyObservable : Observable<ArtistBiography>
    fun getArtistInfo(artistName: String)

}

internal class PresenterImpl(
     private var respository: Repository
) : Presenter{

    override val artistBiographyObservable = Subject<ArtistBiography>()
    override fun getArtistInfo(artistName: String){
        val infoArtist = respository.getArtistInfoFromRepository(artistName)
        artistBiographyObservable.notify(infoArtist)
    }

}