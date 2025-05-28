package ayds.songinfo.moredetails.fulllogic.model

import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository
import ayds.songinfo.moredetails.fulllogic.view.ArtistBiography
import retrofit2.Response

interface HomeModel{
    fun getArtistInfo(artistName: String): ArtistBiography
}

internal class HomeModelImpl(
    private val repository: ArticleRepository
) : HomeModel{

    override fun getArtistInfo(artistName: String): ArtistBiography {
        val artistBiography = repository.getArtistInfoFromRepository(artistName)
        return artistBiography
    }
}

