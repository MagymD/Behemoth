package magym.behemoth.utils

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackbar(text: String, buttonTitle: String = "", funAction: (() -> Unit)? = null) {
    var snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG)

    funAction?.let {
        snackbar = Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE)
                .setAction(buttonTitle, snackbarOnClickListener(funAction))
    }

    snackbar.show()
}

private fun snackbarOnClickListener(funAction: () -> Unit) = View.OnClickListener { funAction() }