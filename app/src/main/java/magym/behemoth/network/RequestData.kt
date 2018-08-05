package magym.behemoth.network

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import magym.behemoth.main.INetworkResult

class RequestData(private val iNetworkResult: INetworkResult) {

    private val networkRequestManager = NetworkRequestManager()

    fun request(url: String) {
        val rss = networkRequestManager.getData(url)

        when (rss) {
            is NetworkRequestResult.ResultNetwork -> {
                rss.rss.channel?.let { launch(UI) { iNetworkResult.addChannel(it, url) } }
            }
            is NetworkRequestResult.Error -> {
                iNetworkResult.showException(rss.error, url)
            }
        }
    }

}