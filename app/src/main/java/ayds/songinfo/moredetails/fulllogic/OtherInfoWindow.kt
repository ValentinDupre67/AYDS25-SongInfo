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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initializeVariables()
        //set api
        // set database
        open(intent.getStringExtra("artistName"))
    }

    private fun initializeVariables() {
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
            //mover
            setDataBase()
            getArtistInfo(artist)
            //updateUI(artistInfo)
        }.start()
    }

    private fun getArtistInfo(artistName: String?) {
        val articleArtistInfo = getArticle(artistName)

        val textArtistInfo = if (articleArtistInfo != null) {
             getArtistBiography(articleArtistInfo)
        } else {
             getInfoArtisOfLastFMAPI(artistName!!)
        }
        //updateUI()   function
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPaneView!!.text = Html.fromHtml(textArtistInfo)
        }
    }

    private fun getArtistBiography(article: ArticleEntity): String {

        var text = "[*]" + article.biography
        val urlString = article.articleUrl
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return text
    }

    private fun getInfoArtisOfLastFMAPI(artistName: String): String {
        var text = ""
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
                text = "No Results"
            } else {
                text = extract.asString.replace("\\n", "\n")

                text = textToHtml(text, artistName)

                val text2 = text
                Thread {
                    //funcion na parte
                    dataBase!!.ArticleDao().insertArticle(
                        ArticleEntity(
                            artistName, text2, url.asString
                        )
                    )
                }.start()
            }
            val urlString = url.asString
            //hacer funcion
            findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(urlString))
                startActivity(intent)
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return text
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
