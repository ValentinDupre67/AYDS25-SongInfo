import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.view.ReleaseDateResolverFactory
import io.mockk.mockk
import org.junit.Test

class SongDescriptionHelperTest{
    private val releaseDateResolverFactory : ReleaseDateResolverFactory = mockk(relaxUnitFun = true)

    @Test
    fun `when call getSongDescriptionText and returns spotifySong stored`(){
        val spotifySong = Song.SpotifySong(
            "mockk",
            "mockk",
            "mockk",
            "mockk",
            "mockk",
            "mockk",
            "mockk",
            "mockk",
            true
        )

    }
}