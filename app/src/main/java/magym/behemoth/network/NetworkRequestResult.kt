package magym.behemoth.network

import magym.behemoth.model.Rss

sealed class NetworkRequestResult {

    class ResultNetwork(val rss: Rss) : NetworkRequestResult()

    class Error(val error: Throwable) : NetworkRequestResult()

}