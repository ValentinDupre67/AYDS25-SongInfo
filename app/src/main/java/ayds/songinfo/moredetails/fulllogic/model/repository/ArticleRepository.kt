package ayds.songinfo.moredetails.fulllogic.model.repository

import ayds.songinfo.moredetails.fulllogic.model.entities.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.model.repository.external.ServiceDataBase
import ayds.songinfo.moredetails.fulllogic.model.repository.local.LocalDataBase
import ayds.songinfo.moredetails.fulllogic.view.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

interface ArticleRepository{
    fun getArtistInfoFromRepository(artistName: String): ArtistBiography
}

internal class ArticleRepositoryImpl(
    private val externalDataBase: ServiceDataBase,
    private var localDataBase: LocalDataBase
): ArticleRepository{


    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography {

        val dbArticle = localDataBase.getArticle(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = externalDataBase.getArticle(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                insertArtistIntoDB(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

    private fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        localDataBase.insertArtistIntoDB(artistBiography)
    }

}