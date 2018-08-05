package magym.behemoth.main

import magym.behemoth.model.Channel

interface INetworkResult {

    fun addChannel(channel: Channel, url: String)

    fun showException(t: Throwable, urlChannel: String)

}