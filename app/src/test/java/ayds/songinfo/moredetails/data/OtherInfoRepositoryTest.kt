import androidx.room.Relation
import ayds.songinfo.moredetails.data.OtherInfoRepositoryImpl
import ayds.songinfo.moredetails.data.external.OtherInfoService
import ayds.songinfo.moredetails.data.local.OtherInfoLocalStorage
import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.domain.OtherInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class OtherInfoRepositoryTest {
    private val otherInfoLocalStorage: OtherInfoLocalStorage = mockk(relaxUnitFun = true)
    private val otherInfoService: OtherInfoService = mockk(relaxUnitFun = true)
    private val otherInfoRepository : OtherInfoRepository = OtherInfoRepositoryImpl(otherInfoLocalStorage,otherInfoService)

    @Test
    fun `when call getArtistInfo and returns ArtistBiography to localStorage`() {
        val mockBiography = ArtistBiography("mockkName","mockkBiography","mockkUrl",false)
        every { otherInfoLocalStorage.getArticle("mockkName") } returns mockBiography

        val result = otherInfoRepository.getArtistInfo("mockkName")

        assertEquals(result, mockBiography.copy(isLocallyStored = true))
        assertTrue(result.isLocallyStored)
    }


    //TODO preguntar por que tendria que hacerlo como el
    //  every { otherInfoLocalStorage.insertArtist(artistBiography) } returns Unit
    @Test
    fun `when call getArtistInfo and returns ArtistBiography to service and biography is Not Empty`(){
        val mockBiography = ArtistBiography("mockkName","mockkBiography","mockkUrl",false)
        every { otherInfoLocalStorage.getArticle("mockkName") } returns null
        every {otherInfoService.getArticle("mockkName")} returns mockBiography

        val result = otherInfoRepository.getArtistInfo("mockkName")
        verify(exactly = 1) { otherInfoLocalStorage.insertArtist(mockBiography)}

        assertEquals(result,mockBiography)
    }

    //todo preguntar esto, para mi esta de mas lo de abajo, si ya esta seteado el mockBiography como falso en isLOcallyStored
    // Assert.assertFalse(result.isLocallyStored)
    // verify(inverse = true) { otherInfoLocalStorage.insertArtist(any()) }
    @Test
    fun `when call getArtistInfo and returns ArtistBiography to service is biography is Empty`(){
        val mockBiography = ArtistBiography("mockkName","","mockkUrl",false)
        every { otherInfoLocalStorage.getArticle("mockkName") } returns null
        every {otherInfoService.getArticle("mockkName")} returns mockBiography

        val result = otherInfoRepository.getArtistInfo("mockkName")
        verify(exactly = 0) { otherInfoLocalStorage.insertArtist(mockBiography)}

        assertEquals(result,mockBiography)
    }
}