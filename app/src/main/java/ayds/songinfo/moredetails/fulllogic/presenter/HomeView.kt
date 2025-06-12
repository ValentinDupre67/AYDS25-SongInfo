package ayds.songinfo.moredetails.fulllogic.presenter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import com.squareup.picasso.Picasso

//Esto lo dejo aca???
private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface HomeView{

}

internal class HomeViewImpl : Activity(), HomeView {
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView
    private lateinit var homePresenter: HomePresenter


    private val homeViewResolver : HomeViewResolver = HomeViewInjector.homeViewResolver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViewProperties()
        initModule()
        initObservers()
        getArtistInfoAsync()
    }

    private fun initModule() {
        HomeViewInjector.init(this)
        homePresenter = HomePresenterInjector.getPresenter()
    }

    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }
    private fun initObservers() {
        homePresenter.songObservable
            .subscribe { value ->
                updateUi(value)
            }
    }

    private fun getArtistInfoAsync() {
        Thread {
            getArtistInfo()
        }.start()
    }

    private fun getArtistInfo() { 
        val artistName = getArtistName()
        homePresenter.getArtistInfo(artistName)
    }

    private fun updateUi(artistBiography: ArtistBiography) {
        runOnUiThread {
            updateOpenUrlButton(artistBiography)
            updateLastFMLogo()
            updateArticleText(artistBiography)
        }
    }

    private fun updateOpenUrlButton(artistBiography: ArtistBiography) {
        openUrlButton.setOnClickListener {
            navigateToUrl(artistBiography.articleUrl)
        }
    }

    private fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun updateLastFMLogo() {
        Picasso.get().load(LASTFM_IMAGE_URL).into(lastFMImageView)
    }

    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")

    private fun updateArticleText(artistBiography: ArtistBiography) {
        val text = artistBiography.biography.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(homeViewResolver.textToHtml(text, artistBiography.artistName))
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}


