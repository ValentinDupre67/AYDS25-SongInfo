package ayds.songinfo.moredetails.fulllogic.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.fulllogic.domain.ArticleDao
import ayds.songinfo.moredetails.fulllogic.domain.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}