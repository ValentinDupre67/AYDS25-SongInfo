import ayds.songinfo.moredetails.domain.ArtistBiography
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.presentation.ArtistBiographyDescriptionHelperImpl
import org.junit.Test

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"
class ArtistBiographyDescriptionHelperTest{

    val artistBiographyDescriptionHelper : ArtistBiographyDescriptionHelper = ArtistBiographyDescriptionHelperImpl()
    @Test
    fun `when call getDescription and artistBiography is not locally stored and return description artist`(){
        val mockkArtistBiography = ArtistBiography("mockkName","mockkBiography","mockkUrl")
        val text = "" + mockkArtistBiography.biography.replace("\\n", "\n")

    }

}