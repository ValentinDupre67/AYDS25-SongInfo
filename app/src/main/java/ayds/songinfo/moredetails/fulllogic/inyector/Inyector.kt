package ayds.songinfo.moredetails.fulllogic.inyector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.data.RepositoryImpl
import ayds.songinfo.moredetails.fulllogic.data.external.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.domain.Repository
import ayds.songinfo.moredetails.fulllogic.presentation.ViewImpl
import ayds.songinfo.moredetails.fulllogic.presentation.Presenter
import ayds.songinfo.moredetails.fulllogic.presentation.PresenterImpl
import ayds.songinfo.moredetails.fulllogic.presentation.View
import ayds.songinfo.moredetails.fulllogic.presentation.ViewHelperImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val ARTICLE_BD_NAME = "database-article"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object Inyector {

    private lateinit var articleDatabase: ArticleDatabase

    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var presenter: Presenter
    private lateinit var respository: Repository
    private lateinit var view: View

    fun init(viewImpl: ViewImpl) {
        initArticleDatabase(viewImpl)
        initLastFMAPI()


        view = ViewImpl(ViewHelperImpl())

        respository = RepositoryImpl(articleDatabase, lastFMAPI)

        presenter = PresenterImpl(respository)
    }

    fun getPresenter(): Presenter {
        return presenter
    }
    private fun initArticleDatabase(otherInfoWindow: Context) {
        articleDatabase =
            Room.databaseBuilder(otherInfoWindow, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
    }

    private fun initLastFMAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

}