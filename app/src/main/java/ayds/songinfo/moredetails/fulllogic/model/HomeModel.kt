package ayds.songinfo.moredetails.fulllogic.model
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.presenter.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.model.entiti.ArticleEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface HomeModel{
    fun getArtistInfoFromRepository(artistName: String): ArtistBiography
}

class HomeModelImpl(
    private val articleDatabase: ArticleDatabase,
    private val lastFMAPI: LastFMAPI) : HomeModel {

    override fun getArtistInfoFromRepository(artistName: String): ArtistBiography {

        //val artistName = getArtistName()

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
    private fun ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")
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
    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()
    private fun getArtistBioFromExternalData(serviceData: String?, artistName: String): ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return ArtistBiography(artistName, text, url.asString)
    }
    private fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }
}