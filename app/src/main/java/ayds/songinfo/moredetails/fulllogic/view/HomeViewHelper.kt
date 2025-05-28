package ayds.songinfo.moredetails.fulllogic.view

import java.util.Locale

interface HomeViewResolver{
    fun textToHtml(text: String, term: String?): String
}

internal class HomeViewResolverImpl : HomeViewResolver {
    override fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }
}

