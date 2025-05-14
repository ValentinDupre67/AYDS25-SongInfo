package ayds.songinfo.moredetails.fulllogic.model

import android.content.Context
import androidx.room.Room.databaseBuilder
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.presenter.HomePresenter
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object HomeModelInjector {

    private lateinit var homeModel: HomeModel
    private lateinit var articleDatabase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI



    fun initHomeModel(homePresenter: HomePresenter) {
        initArticleDatabase(homePresenter)
        initLastFMAPI()
        homeModel = HomeModelImpl(articleDatabase, lastFMAPI)
    }

    fun getHomeModel(): HomeModel = homeModel

    fun initArticleDatabase(homePresenter: HomePresenter) {
        articleDatabase =
            databaseBuilder(homePresenter as Context, ArticleDatabase::class.java, "database-article").build()
    }

    fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

}