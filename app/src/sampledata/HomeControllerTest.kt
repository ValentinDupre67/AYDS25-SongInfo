
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
import ayds.songinfo.home.view.ReleaseDateResolverFactory
import ayds.songinfo.home.view.SongDescriptionHelperImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
class HomeControllerTest {

    private val homeModel: HomeModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<HomeUiEvent>()
    private val homeView: HomeView = mockk(relaxUnitFun = true) {
        every { uiEventObservable } returns onActionSubject
    }

    private val homeController = HomeControllerImpl(homeModel)

    @Before
    fun setup() {
        homeController.setHomeView(homeView)
    }

    @Test
    fun `on search event should search song`() {
        every { homeView.uiState } returns HomeUiState(searchTerm = "song")

        onActionSubject.notify(HomeUiEvent.Search)

        verify { homeModel.searchSong("song") }
    }

    @Test
    fun `on more details event should navigate to more details`() {
        every { homeView.uiState } returns HomeUiState(songId = "id")
        val song: Song.SpotifySong = mockk { every { artistName } returns "artist" }
        every { homeModel.getSongById("id") } returns song

        //En este caso el observer es la condición de entrada (lanza el evento)
        onActionSubject.notify(HomeUiEvent.MoreDetails)

        verify { homeView.navigateToOtherDetails("artist") }
    }

    @Test
    fun `on open song url event should open external link`() {
        every { homeView.uiState } returns HomeUiState(songUrl = "url")

        onActionSubject.notify(HomeUiEvent.OpenSongUrl)

        verify { homeView.openExternalLink("urls") }
    }
}