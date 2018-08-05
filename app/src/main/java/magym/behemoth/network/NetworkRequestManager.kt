package magym.behemoth.network

class NetworkRequestManager {

    internal fun getData(url: String): NetworkRequestResult {
        return NetworkManager.api
                .getRssData(url)
                .map<NetworkRequestResult> { NetworkRequestResult.ResultNetwork(it) }
                .onErrorReturn { t -> NetworkRequestResult.Error(t) }
                .blockingGet()
    }

}