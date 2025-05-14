package ayds.songinfo.moredetails.fulllogic.model.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.fulllogic.model.entiti.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.model.repository.local.ArticleDao

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}

