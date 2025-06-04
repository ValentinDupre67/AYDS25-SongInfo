import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.presentation.OtherInfoPresenterImpl
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class OtherInfoPresenterTest {
    private val repository: OtherInfoRepository = mockk(relaxUnitFun = true)
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk(relaxUnitFun = true)
    private val otherInfoPresenter : OtherInfoPresenter = OtherInfoPresenterImpl(repository,artistBiographyDescriptionHelper)


    @Test
    fun `when call getArtistInfo`() {
        val mockBiography = ArtistBiography("mockkName", "mockkBiography", "mockkUrl", false)
        every { repository.getArtistInfo("mockkName") } returns mockBiography
    }


}