import ayds.songinfo.home.model.HomeModel
import ayds.songinfo.home.view.HomeView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class HomeControllerTest {

    private val homeModel : HomeModel = mockk(relaxUnitFun = true);
    private val homeView : HomeView = mockk(relaxUnitFun = true);

    @Test
    fun `on searchSong() homodel should search song`() {

    }

}