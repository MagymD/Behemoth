package magym.rssreader.main

import magym.rssreader.base.DatabaseManager
import magym.rssreader.model.Channel

class MainModel {

    private val databaseManager = DatabaseManager()

    fun insertChannel(channel: Channel, url: String) = databaseManager.insertChannel(channel, url)

    fun getChannels(): List<Channel> = databaseManager.getChannels()

    fun deleteChannel(idChannel: Int) = databaseManager.deleteChannel(idChannel)

    fun getAllItems() = databaseManager.getAllItems()

    fun getItems(idChannel: Int) = databaseManager.getItems(idChannel)

    fun getSizeChannel(idChannel: Int) = databaseManager.getSizeChannel(idChannel)

    fun getSizeChannel() = databaseManager.getSizeChannel()

}