import ayds.songinfo.home.model.HomeModel
import ayds.songinfo.home.model.HomeModelImpl
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.repository.SongRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class HomeModelTest{

    private val repository : SongRepository = mockk(relaxUnitFun = true)
    private val homeModel : HomeModel = HomeModelImpl(repository)
    @Test
    fun `when call getSongById should return song`(){
        val song : Song = mockk();
        every { repository.getSongById("mockk") } returns song

        val result = homeModel.getSongById("mockk")

        Assert.assertEquals(song, result)
    }
    //no tendira que testear cuando no se le pasa nada tipo "" o null o otro caso??

    @Test
    fun `when call searchSong should call repository to search song`(){
        val song : Song = mockk();
        every { repository.getSongByTerm("mockk") } returns song
        val songTester : (Song) -> Unit = mockk(relaxed = true)

        homeModel.songObservable.subscribe{
            songTester(it)
        }

        homeModel.searchSong("mockk")

        verify { songTester(song) } //que esta pasando aca?
    }
}