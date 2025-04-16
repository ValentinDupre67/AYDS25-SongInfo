package ayds.songinfo.home.model

import android.util.Log
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.repository.SongRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface  HomeModel {

    val songObservable: Observable<Song>

    fun searchSong(term: String)

    fun getSongById(id: String): Song
}

internal class HomeModelImpl(private val repository: SongRepository) : HomeModel {

    override val songObservable = Subject<Song>()

    override fun searchSong(term: String) {
        Log.d("MiEtiqueta2", "Este es el texto que quiero mostrar")
        repository.getSongByTerm(term).let {
            songObservable.notify(it)
        }
    }

    override fun getSongById(id: String): Song = repository.getSongById(id)

}