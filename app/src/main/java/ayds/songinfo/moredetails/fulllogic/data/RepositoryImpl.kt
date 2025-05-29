package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.domain.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.domain.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.domain.Repository
import ayds.songinfo.moredetails.fulllogic.presentation.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

internal class RepositoryImpl(
    private var articleDatabase: ArticleDatabase,
    private var lastFMAPI: LastFMAPI
) : Repository{

    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography {  //TODO chau
        val dbArticle = getArticleFromDB(artistName)

        val artistBiography: ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = getArticleFromService(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                insertArtistIntoDB(artistBiography)
            }
        }
        return artistBiography
    }

    private fun getArticleFromDB(artistName: String): ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    private fun getArticleFromService(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

    private fun insertArtistIntoDB(artistBiography: ArtistBiography) { //TODO chau
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }

    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
}