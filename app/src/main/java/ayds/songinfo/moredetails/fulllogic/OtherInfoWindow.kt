package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
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
    //por que van aca y no fuera de la clase OtherInfoWindows
    private lateinit var textPaneView: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI
    //tendria que tener el dataBases aca?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeVariables()
        open(intent.getStringExtra("artistName"))
    }

    private fun initializeVariables() {
        setContentView(R.layout.activity_other_info)
        textPaneView = findViewById(R.id.textPane1)
        setLastFMAPI()
    }

    private fun setLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        return lastFMAPI
    }

    private fun open(artist: String?) {
        Thread {
            setDataBase()
        }.start()
        getArtistInfo(artist)
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            getInfoOfArticleArtist(artistName)
        }.start()
    }

    private fun getInfoOfArticleArtist(artistName: String?) {
        val articleArtistInfo = getArticle(artistName)
        var textArtistInfo = ""

        if (articleArtistInfo != null) {
            textArtistInfo = getArtistBiography(textArtistInfo, articleArtistInfo)
        } else {
            textArtistInfo = getInfoArtisOfLastFMAPI(artistName!!, textArtistInfo)
        }
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPaneView!!.text = Html.fromHtml(textArtistInfo)
        }
    }

    private fun getArtistBiography(textArtistInfo: String,article: ArticleEntity): String {
        var text = textArtistInfo

        text = "[*]" + article.biography
        val urlString = article.articleUrl
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return text
    }

    private fun getInfoArtisOfLastFMAPI(artistName: String, textArtistInfo: String): String {
        var text1 = textArtistInfo
        val callResponse: Response<String>
        try {
            //por hacer la funcion getArticle tuve que poner !! al artisName wtf??
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].asJsonObject
            val bio = artist["bio"].asJsonObject
            val extract = bio["content"]
            val url = artist["url"]


            if (extract == null) {
                text1 = "No Results"
            } else {
                text1 = extract.asString.replace("\\n", "\n")

                text1 = textToHtml(text1, artistName)

                val text2 = text1
                Thread {
                    dataBase!!.ArticleDao().insertArticle(
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
            e1.printStackTrace()
        }
        return text1
    }

    private fun getArticle(artistName: String?) =
        dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)

    private fun setDataBase() {
        dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
    }

    //Es necesario tener esto?
    companion object {
        const val ARTIST_NAME_EXTRA: String = "artistName"
    }

    //esta se podria separar en dos una que haga el buil y otra que haga el textWithBold
    fun textToHtml(text: String, term: String?): String {
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
