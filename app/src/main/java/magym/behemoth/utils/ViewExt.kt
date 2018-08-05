package magym.behemoth.utils

import android.app.Activity
import android.view.Gravity
import android.widget.TextView

fun Activity.createTextView(): TextView {
    val textView = TextView(this)
    textView.gravity = Gravity.CENTER_VERTICAL

    return textView
}