package magym.rssreader.main

import android.arch.lifecycle.ViewModel
import magym.rssreader.model.RequestNewChannel

class MainViewModel : ViewModel() {

    var news: List<RequestNewChannel> = ArrayList()

    var activeChannelsUrl: List<String> = ArrayList()

}