package ayds.songinfo.moredetails.fulllogic.data

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.domain.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.domain.Repository
import ayds.songinfo.moredetails.fulllogic.presentation.View
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val ARTICLE_BD_NAME = "database-article"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object RepositoryInyector {

    private lateinit var articleDatabase: ArticleDatabase

    private lateinit var lastFMAPI: LastFMAPI

    private lateinit var repository: Repository

    fun init(view: View) {
        initLastFMAPI()
        initArticleDatabase(view)

        repository = RepositoryImpl(articleDatabase, lastFMAPI)
    }
    fun getRepository() = repository

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun initArticleDatabase(view: View) {
        articleDatabase =
            Room.databaseBuilder(view as Context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }
}