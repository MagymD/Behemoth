package magym.behemoth.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val dateFormats = ArrayList<SimpleDateFormat>().apply {
    add(SimpleDateFormat("E, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH))
    add(SimpleDateFormat("dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH))
    add(SimpleDateFormat("E MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH))
    add(SimpleDateFormat("E, dd MMM yyyy HH:mm zzz", Locale.ENGLISH))
}

fun String.dateToLong(): Long {
    dateFormats.forEach {
        try {
            return it.parse(this).time
        } catch (ignored: ParseException) {
        }
    }

    return 0
}