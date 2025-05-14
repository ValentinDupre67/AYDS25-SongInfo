package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
class OtherInfoWindow : Activity() {
    private lateinit var textPaneView: TextView
    private lateinit var dataBase: ArticleDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        setTextPanelView()
        setDatabase()
        open()
    }

    private fun setDatabase() {
        dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
        }.start()
    }
    private fun setTextPanelView() {
        textPaneView = findViewById(R.id.textPane1)
    }

    private fun open() {
        val artist = getArtist();
        setArtistInfoOnViewMoreDetails(artist)
    }
    private fun setArtistInfoOnViewMoreDetails(artistName: String){
        Thread {
            val textInfoArtist = getArticleInformationFromDataBase(artistName)
            setArtistInformationOnView(textInfoArtist)
        }.start()
    }

    private fun getArticleInformationFromDataBase(artistName: String): String {
        val article = getArticle(artistName)
        var textInfoArtist = ""
        textInfoArtist = if (article != null) { // exists in db
            getArticleInformationOfLocalDataBase(article)
        } else { // get from service
            getRticleInformationFromService(artistName)
        }
        return textInfoArtist
    }

    private fun setArtistInformationOnView(finalText: String) {
        runOnUiThread {
            setImageUrlInApp()
            textPaneView.text = Html.fromHtml(finalText)
        }
    }

    private fun setImageUrlInApp() {
        Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
    }

    private fun getRticleInformationFromService(artistName: String): String {
        val lastFMAPI = getLastFMAPI()
        var textInfoArtist1 = ""
        val callResponse: Response<String>
        try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].asJsonObject
            val bio = artist["bio"].asJsonObject
            val extract = bio["content"]
            val url = artist["url"]

            if (extract == null) {
                textInfoArtist1 = "No Results"
            }else{
                textInfoArtist1 = extract.asString.replace("\\n", "\n")
                textInfoArtist1 = textToHtml(textInfoArtist1, artistName)
                // save to DB  <o/
                val text2 = textInfoArtist1
                Thread {
                    dataBase.ArticleDao().insertArticle(
                        ArticleEntity(
                            artistName, text2, url.asString
                        )
                    )
                }.start()
            }

            val urlString = url.asString
            findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(urlString))
                startActivity(intent)
            }
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return textInfoArtist1
    }

    private fun getArticle(artistName: String?): ArticleEntity? {
        val article = dataBase.ArticleDao().getArticleByArtistName(artistName!!)
        return article
    }
    private fun getArticleInformationOfLocalDataBase(article: ArticleEntity): String {
        var textInfoArtist1 = ""
        textInfoArtist1 = "[*]" + article.biography
        val urlString = article.articleUrl
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return textInfoArtist1
    }
    private fun getLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        return lastFMAPI
    }
    private fun getArtist(): String {
        val artis = intent.getStringExtra("artistName")
        return artis ?: ""
    }
    companion object {
        const val ARTIST_NAME_EXTRA: String = "artistName"
    }
    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()

        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")

        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )

        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}
