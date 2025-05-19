package ayds.songinfo.moredetails.fulllogic.model

import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository
import retrofit2.Response

interface HomeModel{
    fun getsong(artistName: String): Response<String>
}

internal class HomeModelImpl(
    private val repo: ArticleRepository
) : HomeModel{

    override fun getsong(artistName: String): Response<String> {
        return repo.getSongFromService(artistName)
    }

}

