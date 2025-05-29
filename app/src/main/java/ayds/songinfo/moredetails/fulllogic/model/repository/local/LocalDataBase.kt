package ayds.songinfo.moredetails.fulllogic.model.repository.local

import ayds.songinfo.moredetails.fulllogic.model.entities.ArticleEntity
import ayds.songinfo.moredetails.fulllogic.model.repository.local.room.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.view.ArtistBiography

interface LocalDataBase{
    fun getArticle(artistName: String): ArtistBiography?
    fun insertArtistIntoDB(artistBiography: ArtistBiography)
}

internal class LocalDataBaseImpl(
    private var localDataBase: ArticleDatabase
) : LocalDataBase{

    override fun getArticle(artistName: String): ArtistBiography? {
        val artistEntity = localDataBase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    override fun insertArtistIntoDB(artistBiography: ArtistBiography) {
        localDataBase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }
}