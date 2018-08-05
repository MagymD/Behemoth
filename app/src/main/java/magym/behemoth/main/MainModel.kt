package magym.behemoth.main

import magym.behemoth.base.DatabaseManager
import magym.behemoth.model.Channel

class MainModel {

    private val databaseManager = DatabaseManager()

    internal fun insertChannel(channel: Channel, url: String) = databaseManager.insertChannel(channel, url)

    internal fun getChannels(): List<Channel> = databaseManager.getChannels()

    internal fun deleteChannel(idChannel: Int) = databaseManager.deleteChannel(idChannel)

    internal fun getAllItems() = databaseManager.getAllItems()

    internal fun getItems(idChannel: Int) = databaseManager.getItems(idChannel)

    internal fun getSizeChannel(idChannel: Int) = databaseManager.getSizeChannel(idChannel)

    internal fun getSizeChannel() = databaseManager.getSizeChannel()

}