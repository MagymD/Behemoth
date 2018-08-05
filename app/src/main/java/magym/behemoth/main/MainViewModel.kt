package magym.behemoth.main

import android.arch.lifecycle.ViewModel
import magym.behemoth.model.RequestNewChannel

class MainViewModel : ViewModel() {

    internal var news: List<RequestNewChannel> = ArrayList()

    internal var activeChannelsUrl: List<String> = ArrayList()

}