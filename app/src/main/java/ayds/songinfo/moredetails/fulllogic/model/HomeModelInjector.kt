package ayds.songinfo.moredetails.fulllogic.model

import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepository
import ayds.songinfo.moredetails.fulllogic.model.repository.ArticleRepositoryImpl
import ayds.songinfo.moredetails.fulllogic.model.repository.external.LastFMAPI
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
object HomeModelInjector {

    private lateinit var homeModel: HomeModel
    private lateinit var lastFMAPI: LastFMAPI

    fun init() {
        initLastFMAPI()

        val repo: ArticleRepository = ArticleRepositoryImpl(lastFMAPI)

        homeModel = HomeModelImpl(repo)
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