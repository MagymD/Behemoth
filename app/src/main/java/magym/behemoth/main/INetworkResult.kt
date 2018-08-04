package magym.rssreader.main

import magym.rssreader.model.Channel

interface INetworkResult {

    fun addChannel(channel: Channel, url: String)

    fun showException(t: Throwable, urlChannel: String)

}