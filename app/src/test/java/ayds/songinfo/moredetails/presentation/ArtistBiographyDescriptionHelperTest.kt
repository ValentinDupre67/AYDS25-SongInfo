import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"
class ArtistBiographyDescriptionHelperTest{

    val artistBiographyDescriptionHelper : ArtistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()
    @Test
    fun `when call getDescription and artistBiography is locally stored and return description artist`(){
        val mockkArtistBiography = ArtistBiography("mockkName","mockkBiography","mockkUrl",isLocallyStored = true)
        val text = "$HEADER[*]mockkBiography$FOOTER"

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }

    @Test
    fun `when call getDescription and artistBiography is not locally stored and return description artist`(){
        val mockkArtistBiography = ArtistBiography("mockkName","mockkBiography","mockkUrl",isLocallyStored = false)
        val text = HEADER+"mockkBiography"+FOOTER

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }

    @Test
    fun `when call getDescription and artistBiography with ' ' and return description artist refactoriced`(){
        val mockkArtistBiography = ArtistBiography("mockkName","'mockkBiography comillas'","mockkUrl",isLocallyStored = false)
        val text = "$HEADER mockkBiography comillas $FOOTER"

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }

    @Test
    fun `when call getDescription and artistBiography with (doble barra + n) and return description artist refactoriced`(){
        val mockkArtistBiography = ArtistBiography("mockkName","\\n mockkBiography comillas \\n","mockkUrl",isLocallyStored = false)
        val text = "$HEADER<br> mockkBiography comillas <br>$FOOTER"

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }

    @Test
    fun `when call getDescription and artistBiography with (barra + n) and return description artist refactoriced`(){
        val mockkArtistBiography = ArtistBiography("mockkName","\n mockkBiography comillas \n","mockkUrl",isLocallyStored = false)
        val text = "$HEADER<br> mockkBiography comillas <br>$FOOTER"

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }

    @Test
    fun `when call getDescription and artistBiography with (i)$term and return description artist refactoriced`(){
        val mockkArtistBiography = ArtistBiography("mockkName","mockkBiography mockkName","mockkUrl",isLocallyStored = false)
        val text = HEADER+ "mockkBiography <b>MOCKKNAME</b>" +FOOTER

        val result = artistBiographyDescriptionHelper.getDescription(mockkArtistBiography)

        Assert.assertEquals(result,text)
    }
}