package magym.rssreader.base

import magym.rssreader.model.Channel
import magym.rssreader.model.New
import magym.rssreader.utils.dateToLong

class DatabaseManager {

    private val db = App.instance.database
    private val daoChannel by lazy { db.daoChannel() }
    private val daoNew by lazy { db.daoNew() }

    fun insertChannel(channel: Channel, url: String) {
        with(channel) {
            id = channel.titleChannel.hashCode()
            this.url = url
            urlIcoChannel = getIcoUrl(url)
        }

        daoChannel.insertElement(channel)

        val items = channel.news

        items?.let { insertItems(it, channel.id) }
    }

    fun getChannels(): List<Channel> = daoChannel.getAll()

    fun deleteChannel(idChannel: Int) {
        daoChannel.deleteElement(idChannel)
        deleteItems(idChannel)
    }

    fun getAllItems() = daoNew.getAllNewsChannel()

    fun getItems(idChannel: Int) = daoNew.getNewsChannel(idChannel)

    fun getSizeChannel(idChannel: Int) = daoNew.getSizeChannel(idChannel)

    fun getSizeChannel() = daoNew.getSizeChannel()

    private fun insertItems(news: List<New>, idChannel: Int) {
        news.forEach {
            with(it) {
                id = it.title.hashCode() // TODO: Формировать id другим образом, учитывать возможные коллизии
                this.idChannel = idChannel
                dateLong = date.dateToLong()
            }
        }

        daoNew.insertElements(news)
    }

    private fun deleteItems(idChannel: Int) = daoNew.deleteElements(idChannel)

    private fun getIcoUrl(url: String): String {
        val icoUrl = StringBuilder(url)
        for (i in 7 until icoUrl.length) {
            if (url[i] == '/') {
                icoUrl.delete(i, icoUrl.length)
                icoUrl.append("/favicon.ico")
                break
            }
        }
        return icoUrl.toString()
    }

}