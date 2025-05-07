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

//comentario general: tengo que prestar atencion cuando voy a separar funciones
//de no mesclar logica de bases de datos, de la vista, logica pura, actulizar la UI etc
// en caso de pasar esto hai es cuando tendria que separa el respectivo codigo en funciones

private const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindow : Activity() {
    private lateinit var textPaneView: TextView
    private lateinit var dataBase: ArticleDatabase
    private lateinit var lastFMAPI: LastFMAPI

    //el profe dijo que es emjor hacer setApi() y SetDataBses() por separado
    // y no dentro del initializeVariables() ya que son datos o variables que
    //no tiene que ver estaria mesclando cosas de datos con cosas de la view con cosas de la API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initializeVariables()
        setLastFMAPI()
        setDataBase()
        //intent.getStringExtra("artistName") esto tendria que sacarlo y ver que no se null, en caso de ser
        // no tengo que hacer nada, y en caso de que no sean null hago el open
        open(intent.getStringExtra("artistName"))
    }

    private fun initializeVariables() {
        //TODO aca tal vez tendria que estar el tema de los botones de la view o los lisenings
        textPaneView = findViewById(R.id.textPane1)
    }

    private fun setLastFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        return lastFMAPI
    }

    private fun setDataBase() {
        dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
    }

    private fun open(artist: String) {
        Thread {
            //updateUI(artistInfo)
            updateUI(getArtistInfo(artist))
        }.start()
    }

    private fun getArtistInfo(artistName: String): String {
        val articleArtistInfo = getArticle(artistName)

        return if (articleArtistInfo != null) {
             getArtistBiography(articleArtistInfo)
        } else {
             getInfoArtisOfLastFMAPI(artistName!!)
        }
    }

    private fun updateUI(textArtistInfo: String) {
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

    //Es necesario tener esto?
    //si es necesario tener esto ya que la viu accede a este dato usando ARTIST_NAME_EXTRA
    companion object {
        const val ARTIST_NAME_EXTRA: String = "artistName"
    }

    //esta se podria separar en dos una que haga el buil y otra que haga el textWithBold
    // asi como esta el profe dijo que estaba bien que no hace falta separarla, se podria hacer
    // osea no estaria mal pero no hace falta
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
