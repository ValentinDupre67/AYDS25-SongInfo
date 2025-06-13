package ayds.songinfo.moredetails.fulllogic.presentation

import java.util.Locale

interface ViewHelper {
    fun textToHtml(text: String, term: String?): String
}

internal class ViewHelperImpl () :ViewHelper {

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