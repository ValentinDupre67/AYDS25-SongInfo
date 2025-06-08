import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyUiState
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {
    private val repository: OtherInfoRepository = mockk(relaxUnitFun = true)
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxUnitFun = true)
    private val otherInfoPresenter : OtherInfoPresenter = OtherInfoPresenterImpl(repository,artistBiographyDescriptionHelper)


    @Test
    fun `when call getArtistInfo`() {
        val mockBiography = ArtistBiography("mockkName", "mockkBiography", "mockkUrl", false)
        every { repository.getArtistInfo("mockkName") } returns mockBiography
        every { artistBiographyDescriptionHelper.getDescription(mockBiography) } returns "mockkDescription"
        val mockArtistBiographyUiState : (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.artistBiographyObservable.subscribe(mockArtistBiographyUiState)
        otherInfoPresenter.getArtistInfo("mockkName")

       val result = ArtistBiographyUiState("mockkName", "mockkDescription", "mockkUrl")
       verify { mockArtistBiographyUiState(result) }
    }


}