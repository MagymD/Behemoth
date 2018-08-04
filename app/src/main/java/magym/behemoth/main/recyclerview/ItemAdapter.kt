package magym.rssreader.main.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import magym.rssreader.R
import magym.rssreader.main.IOnClick
import magym.rssreader.model.RequestNewChannel

class ItemAdapter(private val news: ArrayList<RequestNewChannel>,
                  private val iOnClick: IOnClick)
    : RecyclerView.Adapter<ItemViewHolder>() {

    init {
        // notifyDataSetChanged()
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.new_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(news, position)

        holder.itemChannel.setOnClickListener { iOnClick.openChannel(news[position].idChannel) }

        holder.browser.setOnClickListener { iOnClick.openBrowser(news[position].link) }

        holder.image.setOnClickListener { iOnClick.openPhoto(news[position].urlImage) }
    }

    override fun getItemId(position: Int): Long = news[position].id.toLong()

    override fun getItemCount() = news.size

}