package ayds.songinfo.moredetails.fulllogic.model.repository.external

import ayds.songinfo.moredetails.fulllogic.model.repository.external.auth.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.view.ArtistBiography
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

interface ServiceDataBase {

    fun getArticle(artistName: String): ArtistBiography
}

internal class ServiceDataBaseImpl(
    private val LastFMAPIService: LastFMAPI,
) : ServiceDataBase {

    override fun getArticle(artistName: String): ArtistBiography {

        var artistBiography = ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getSongFromService(artistName: String): Response<String> =
        LastFMAPIService.getArtistInfo(artistName).execute()

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
}