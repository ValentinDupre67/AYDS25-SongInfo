package ayds.songinfo.moredetails.fulllogic.domain

import ayds.songinfo.moredetails.fulllogic.presentation.ArtistBiography

interface Repository{

    fun getArtistInfoFromRepository(artistName: String): ArtistBiography
}