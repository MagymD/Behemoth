package magym.rssreader.network

import magym.rssreader.model.Rss

sealed class NetworkRequestResult {

    class ResultNetwork(val rss: Rss) : NetworkRequestResult()

    class Error(val error: Throwable) : NetworkRequestResult()

}