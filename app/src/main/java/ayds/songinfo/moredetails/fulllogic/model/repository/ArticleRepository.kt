package ayds.songinfo.moredetails.fulllogic.model.repository

import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI
import retrofit2.Response

interface ArticleRepository{
    fun getSongFromService(artistName: String): Response<String>
}

internal class ArticleRepositoryImpl(
    private val lastFMAPI: LastFMAPI
): ArticleRepository{
    override fun getSongFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()
}