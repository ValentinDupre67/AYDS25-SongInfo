package ayds.songinfo.moredetails.fulllogic.model.repository.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ayds.songinfo.moredetails.fulllogic.domain.entities.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.model.repository.local.ArticleDao

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}

