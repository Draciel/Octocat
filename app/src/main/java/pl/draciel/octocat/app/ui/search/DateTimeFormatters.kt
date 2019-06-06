package pl.draciel.octocat.app.ui.search

import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

object DateTimeFormatters {

    @JvmStatic
    val CODE_REPOSITORY_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

}
