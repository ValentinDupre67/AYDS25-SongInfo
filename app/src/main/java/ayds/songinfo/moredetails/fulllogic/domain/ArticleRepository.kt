package ayds.songinfo.moredetails.fulllogic.domain

import ayds.songinfo.moredetails.fulllogic.presenter.ArtistBiography

interface ArticleRepository{
    fun getArtistInfo(artistName: String): ArtistBiography
    //fun getArtistInfoFromRepository(artistName: String): ArtistBiography
}