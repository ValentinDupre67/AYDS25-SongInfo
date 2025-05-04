package ayds.songinfo.moredetails.fulllogic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import ayds.songinfo.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;


import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OtherInfoWindow extends Activity {

  public final static String ARTIST_NAME_EXTRA = "artistName";
  public final static String IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png";

  //preguntar que es mejor crearlas y pasarlas por parametro o crear la variable y luego setearla en el onCreate
  private ArticleDatabase dataBase;

  //preguntar que es mejor crearlas y pasarlas por parametro o crear la variable y luego setearla en el onCreate
  private TextView textPane1;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_other_info);
    setTextPanel();
    setDataBase();
    open(getIntent().getStringExtra("artistName"));
  }
  private void setTextPanel() {
    textPane1 = findViewById(R.id.textPane1);
  }

  private void setDataBase() {
    dataBase =    Room.databaseBuilder(this, ArticleDatabase.class, "database-name-thename").build();
  }

  private void open(String artist) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        dataBase.ArticleDao().insertArticle(new ArticleEntity( "test", "sarasa", "")  );
      }
    }).start();


    getARtistInfo(artist);
  }

  public void getARtistInfo(String artistName) {
    LastFMAPI lastFMAPI = getLastFMAPI();
    new Thread(new Runnable() {
          @Override
          public void run() {
            ArticleEntity article = getArticleEntity(artistName);
            String text = "";

            if (article != null) {

              text = "[*]" + article.getBiography();

              final String urlString = article.getArticleUrl();
              findViewById(R.id.openUrlButton1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setData(Uri.parse(urlString));
                  startActivity(intent);
                }
              });

            } else { // get from service
              Response<String> callResponse;
              try {
                callResponse = lastFMAPI.getArtistInfo(artistName).execute();

                Gson gson = new Gson();
                JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                JsonObject artist = jobj.get("artist").getAsJsonObject();
                JsonObject bio = artist.get("bio").getAsJsonObject();
                JsonElement extract = bio.get("content");
                JsonElement url = artist.get("url");


                if (extract == null) {
                  text = "No Results";
                } else {
                  text = extract.getAsString().replace("\\n", "\n");

                  text = textToHtml(text, artistName);

                  // save to DB  <o/
                  final String text2 = text;
                  new Thread(new Runnable() {
                    @Override
                    public void run() {
                      dataBase.ArticleDao().insertArticle(new ArticleEntity(artistName, text2, url.getAsString()));
                    }
                  }).start();
                }

                final String urlString = url.getAsString();
                findViewById(R.id.openUrlButton1).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlString));
                    startActivity(intent);
                  }
                });

              } catch (IOException e1) {
                e1.printStackTrace();
              }
            }
            final String finalText = text;

            runOnUiThread( () -> {
              Picasso.get().load(IMAGE_URL).into((ImageView) findViewById(R.id.imageView1));
              textPane1.setText(Html.fromHtml( finalText));
            });



          }
        }).start();

  }

  @Nullable
  private ArticleEntity getArticleEntity(String artistName) {
    ArticleEntity article = dataBase.ArticleDao().getArticleByArtistName(artistName);
    return article;
  }

  @NonNull
  private static LastFMAPI getLastFMAPI() {
    Retrofit retrofit = buildRetroFit();
    return retrofit.create(LastFMAPI.class);
  }

  @NonNull
  private static Retrofit buildRetroFit() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    return retrofit;
  }

  public static String textToHtml(String text, String term) {

    StringBuilder builder = new StringBuilder();

    setBuilder(text, term, builder);

    return builder.toString();
  }

  private static void setBuilder(String text, String term, StringBuilder builder) {
    builder.append("<html><div width=400>");
    builder.append("<font face=\"arial\">");


    String textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");

    builder.append(textWithBold);

    builder.append("</font></div></html>");
  }


}
