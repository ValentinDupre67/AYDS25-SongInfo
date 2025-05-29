package ayds.songinfo.moredetails.fulllogic.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ayds.songinfo.moredetails.fulllogic.domain.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: ArticleEntity)

    @Query("SELECT * FROM Articleentity WHERE artistName LIKE :artistName LIMIT 1")
    fun getArticleByArtistName(artistName: String): ArticleEntity?

}