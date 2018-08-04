package magym.rssreader.main

import magym.rssreader.model.Channel
import magym.rssreader.model.RequestNewChannel

interface IMainView {

    // Toolbar

    fun setTitleToolbar(title: String)

    fun showToolbar()

    fun enableItemToolbarDeleteChannel(enabled: Boolean)

    // SwipeRefreshLayout

    fun setRefreshing(refreshing: Boolean)

    // NavigationView

    fun addChannelToMenu(channel: Channel, checked: Boolean)

    fun deleteChannelFromMenu(id: Int)

    fun checkAllChannelsFromMenu()

    fun checkMenuItem(id: Int)

    fun updateCounter(id: Int, text: String)

    // RecyclerView

    fun updateDataSetChanged()

    fun scrollToPosition(position: Int)

    // NetworkResult

    fun insertElementsIntoView(newListNews: MutableList<RequestNewChannel>)

    fun showException(t: Throwable, urlChannel: String)

    // Snackbar

    fun createSnackbar(text: String)

    fun channelsNotFound()

    // ViewModel

    fun updateItemsFromViewModel()

    fun setActiveChannelsUrlIntoViewModel(activeChannelsUrl: List<String>)

    // Internet

    fun hasConnection(): Boolean

}