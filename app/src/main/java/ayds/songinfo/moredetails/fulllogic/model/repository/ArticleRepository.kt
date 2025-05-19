package ayds.songinfo.moredetails.fulllogic.model.repository

import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI

interface ArticleRepository{
    fun getSongFromService(artistName: String): Any
}

internal class ArticleRepositoryImpl(
    private val lastFMAPI: LastFMAPI)

    : ArticleRepository{
    override fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
}