package magym.behemoth.main.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.github.marlonlom.utilities.timeago.TimeAgo
import magym.behemoth.R
import magym.behemoth.model.RequestNewChannel
import magym.behemoth.utils.fromHtml
import magym.behemoth.utils.load

class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val imageChannel: ImageView = v.findViewById<View>(R.id.item_image_channel) as ImageView
    private val date: TextView = v.findViewById<View>(R.id.item_date) as TextView
    private val title: TextView = v.findViewById<View>(R.id.item_title) as TextView
    private val description: TextView = v.findViewById<View>(R.id.item_description) as TextView

    internal val itemChannel: RelativeLayout = v.findViewById<View>(R.id.item_channel) as RelativeLayout
    internal val browser: ImageView = v.findViewById<View>(R.id.item_browser) as ImageView
    internal val image: ImageView = v.findViewById<View>(R.id.item_image) as ImageView

    internal fun bind(news: List<RequestNewChannel>, position: Int) {
        date.text = TimeAgo.using(news[position].dateLong) // TODO: Обновлять поле, если значения не равны
        description.text = news[position].description.fromHtml()
        title.text = news[position].title.fromHtml()

        setImage(news[position].urlImage, R.drawable.placeholder)
        imageChannel.load(news[position].urlIcoChannel, R.drawable.ic_folder)
    }

    private fun setImage(url: String, idErrorImage: Int) {
        if (url == "http://") {
            image.visibility = View.GONE
        } else {
            image.visibility = View.VISIBLE

            image.load(url, idErrorImage)
        }
    }

}