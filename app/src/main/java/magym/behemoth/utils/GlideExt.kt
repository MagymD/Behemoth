package magym.rssreader.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.Menu
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import magym.rssreader.R

fun ImageView.load(url: String, idErrorImage: Int) {
    Glide.with(this) // TODO: centerInside
            .load(url)
            .thumbnail(Glide.with(this).load(idErrorImage))
            .into(this)
}

fun Menu.loadMenuIco(id: Int, url: String, activity: Activity) {
    this.findItem(id)?.let {
        Glide.with(activity)
                .load(url)
                .thumbnail(Glide.with(activity).load(R.drawable.ic_folder))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(@NonNull drawable: Drawable, @Nullable transition: Transition<in Drawable>?) {
                        it.icon = drawable
                    }
                })
    }
}