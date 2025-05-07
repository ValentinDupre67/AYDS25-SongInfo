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


val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
class OtherInfoWindow : Activity() {
    private lateinit var textPaneView: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

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

    private fun getArtistInfo(artistName: String?) {
        Thread {
            getInfoOfArtist(artistName)
        }.start()
    }

    private fun getInfoOfArtist(artistName: String?) {
        val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
        var text = ""


        if (article != null) {

            text = "[*]" + article.biography

            val urlString = article.articleUrl
            findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(urlString))
                startActivity(intent)
            }
        } else { // get from service
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
                    text = "No Results"
                } else {
                    text = extract.asString.replace("\\n", "\n")

                    text = textToHtml(text, artistName)


                    // save to DB  <o/
                    val text2 = text
                    Thread {
                        dataBase!!.ArticleDao().insertArticle(
                            ArticleEntity(
                                artistName, text2, url.asString
                            )
                        )
                    }
                        .start()
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
        }
        val finalText = text
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
            textPaneView!!.text = Html.fromHtml(finalText)
        }
    }

    private fun open(artist: String?) {
        Thread {
            setDataBase()
        }.start()
        getArtistInfo(artist)
    }

    private fun setDataBase() {
        dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
    }

    //Es necesario tener esto?
    companion object {
        const val ARTIST_NAME_EXTRA: String = "artistName"
    }

    //esta se podria separar en dos una que haga el buil y otra que haga el textWithBold no?
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
