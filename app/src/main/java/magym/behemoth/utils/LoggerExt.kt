package magym.rssreader.utils

import android.app.Activity
import java.util.logging.Level
import java.util.logging.Logger

private const val startText: String = "My log: "

fun Activity.log(text: String = "", t: Throwable? = null) {
    val log = Logger.getLogger(this::class.java.name)

    t?.let {
        log.log(Level.SEVERE, startText + text, t)
        return
    }

    log.info(startText + text)
}

fun Logger.log(text: String = "", t: Throwable? = null) {
    t?.let {
        this.log(Level.SEVERE, startText + text, t)
        return
    }

    this.info(startText + text)
}