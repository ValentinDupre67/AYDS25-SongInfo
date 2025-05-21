package ayds.songinfo.moredetails.fulllogic.model

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.model.repository.local.room.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.view.HomeView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTICLE_BD_NAME = "database-article"
object HomeModelInjector {

    private lateinit var homeModel: HomeModel
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var articleDatabase: ArticleDatabase

    fun init(context: HomeView) {
        initLastFMAPI()
        initArticleDatabase(context)

        val repo: ArticleRepository = ArticleRepositoryImpl(lastFMAPI, articleDatabase)

        homeModel = HomeModelImpl(repo)
    }

    private fun initArticleDatabase(context: HomeView) {
        articleDatabase =
            Room.databaseBuilder(context as Context, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

//    val repository: SongRepository =
//        SongRepositoryImpl(spotifyLocalRoomStorage, spotifyTrackService)
//
//    homeModel = HomeModelImpl(repository)


}