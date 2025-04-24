package ayds.songinfo.home.view

import android.util.Log
import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.model.entities.Song.SpotifySong
interface ReleaseDateHelper {
    fun getReleaseDate(song: Song = EmptySong): String
}

internal class ReleaseDateImpl : ReleaseDateHelper{
    override fun getReleaseDate(song: Song): String {
        val precision = getPrecision(song)
        Log.d("pepe","hola")
        return getFormatedDate(precision, song as SpotifySong)
    }

    private fun getPrecision(song: Song): String {
        val precision = song as SpotifySong
        return precision.releaseDatePrecision
    }

    private fun getFormatedDate(presition: String, song: SpotifySong): String {
        return when (presition) {
            "day" -> return song.releaseDate
            "month" -> getMonthFormat(song.releaseDate)
            "year" -> return song.releaseDate + "(not a leap year)"
            else -> return "-"
        }
    }

    private fun getMonthFormat(date: String): String {
            val parts = date.split("-")
            if (parts.size != 2) return "Invalid date"

            val year = parts[0].toIntOrNull()
            val month = parts[1].toIntOrNull()

            if (year == null || month == null || month !in 1..12) return "Invalid date"

            val monthNames = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )

            val nextYear = year + 1
            val monthName = monthNames[month - 1]

            return "$monthName, $nextYear"
    }
}