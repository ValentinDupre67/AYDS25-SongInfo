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
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

const val ARTIST_NAME_EXTRA: String = "artistName"
const val IMAGE_URL: String ="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindow : Activity() {
    //preguntar que es mejor crearlas y pasarlas por parametro o crear la variable y luego setearla en el onCreate
    private lateinit var dataBase: ArticleDatabase

    //preguntar que es mejor crearlas y pasarlas por parametro o crear la variable y luego setearla en el onCreate
    private lateinit var textView: TextView

    private lateinit var lastFMAPI: LastFMAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        setTextPanel()
        setDataBase()
        open(intent.getStringExtra("artistName"))
    }

    private fun setContentView() {
        setContentView(R.layout.activity_other_info)
    }
    private fun setTextPanel() {
        textView = findViewById(R.id.textPane1)
    }

    private fun setDataBase() {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
    }

    private fun open(artist: String?) {
        Thread { dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", "")) }
            .start()
        getArtistInfo(artist)
    }

    fun getArtistInfo(artistName: String?)  {
        Thread {
            val article = getArticleEntity(artistName)
            var text = ""
            text = if (article != null) {
                getBiography(article)
            } else {
                getFromService(artistName)
            }
            val finalText = text
            runOnUiThread {
                Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
                textView!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun getFromService(artistName: String?): String {
        val lastFMAPI = lastFMAPI //se lo paso por parametro o lo creo aca
        var text = "" //se lo paso por parametro o lo creo aca

        try {
            val callResponse = lastFMAPI.getArtistInfo(artistName!!).execute()
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
                    dataBase!!.ArticleDao()
                        .insertArticle(ArticleEntity(artistName, text2, url.asString))
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
        return text
    }

    private fun getBiography(article: ArticleEntity): String {
        val text = "[*]" + article.biography

        val urlString = article.articleUrl
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return text
    }

    private fun getArticleEntity(artistName: String?): ArticleEntity? {
        val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
        return article
    }

    companion object {
        private val lastFMAPI: LastFMAPI

        get() {
            val retrofit = buildRetroFit()
            return retrofit.create(LastFMAPI::class.java)
        }

        private fun buildRetroFit(): Retrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://ws.audioscrobbler.com/2.0/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
            return retrofit
        }
    }
    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        setBuilder(text, term, builder)
        return builder.toString()
    }
    private fun setBuilder(text: String, term: String?, builder: StringBuilder) {
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
    }
}
