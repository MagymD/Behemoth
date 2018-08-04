package magym.rssreader.main

import android.text.TextUtils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import magym.rssreader.R
import magym.rssreader.model.Channel
import magym.rssreader.model.RequestNewChannel
import magym.rssreader.network.RequestData
import magym.rssreader.utils.getMenuId
import java.util.*

class MainPresenter(private val iMainPresenter: IMainPresenter) : INetworkResult {

    private var channels: MutableList<Channel> = ArrayList()
    private var activeChannelsUrl: MutableList<String> = ArrayList()

    private val requestData = RequestData(this)
    private val mainModel = MainModel()

    init {
        channels.addAll(mainModel.getChannels()) // Main Thread!
    }

    fun setViewModel(mainViewModel: MainViewModel) {
        if (mainViewModel.news.isEmpty()) {
            setActiveChannelsUrl()
            requestItemsFromDb(false)
            requestData()
        } else {
            iMainPresenter.updateItemsFromViewModel()
            activeChannelsUrl.addAll(mainViewModel.activeChannelsUrl)
            iMainPresenter.setTitleToolbar(getTitleByActiveChannelsUrlChannel())
            iMainPresenter.updateDataSetChanged()
            updateAllCounter()
        }
    }

    fun enterPositiveButton(url: String) {
        channels.filter { it.url == url }
                .forEach {
                    channelInList(it)
                    return
                }

        requestData(url)
    }

    fun requestData(url: String = "") {
        if (!iMainPresenter.hasConnection()) {
            requestItemsFromDb(true)
            return
        }

        val isUrlEmpty = TextUtils.isEmpty(url)

        if (channels.isNotEmpty() || !isUrlEmpty) {
            setRefreshing(true)

            val wait = async {
                if (isUrlEmpty) {
                    try {
                        activeChannelsUrl.forEach {
                            requestData.request(it)
                        }
                    } catch (e: ConcurrentModificationException) {
                        e.printStackTrace()
                    }
                } else {
                    requestData.request(url)
                }
            }

            launch(UI) {
                wait.await()
                requestItemsFromDb(true)
            }

        } else {
            iMainPresenter.channelsNotFound()
            setRefreshing(false)
        }
    }

    fun addChannelsToMenu() {
        channels.forEach { iMainPresenter.addChannelToMenu(it, false) }
    }

    fun selectAllChannels() {
        val result = async {
            mainModel.getAllItems()
        }

        launch(UI) { insertElementsIntoView(result.await(), true) }

        setActiveChannelsUrl()
        iMainPresenter.setTitleToolbar("Все новости")
        goToTop(false)
    }

    fun findChannelByMenuItem(itemId: Int) {
        channels.filter { it.getMenuId() == itemId }
                .forEach { findChannel(it) }
    }

    fun findChannelByIdChannel(idChannel: Int) {
        channels.filter { it.id == idChannel }
                .forEach { findChannel(it) }
    }

    fun deleteActiveChannel() {
        if (activeChannelsUrl.size == 1) {
            lateinit var channel: Channel

            channels.filter { it.url == activeChannelsUrl[0] }
                    .forEach {
                        channel = it
                        channels.remove(it)
                        return@forEach
                    }

            val wait = async {
                mainModel.deleteChannel(channel.id)
            }

            launch(UI) {
                wait.await()
                iMainPresenter.deleteChannelFromMenu(channel.getMenuId())
                iMainPresenter.checkAllChannelsFromMenu()
                selectAllChannels()
            }
        }
    }

    override fun addChannel(channel: Channel, url: String) {
        mainModel.insertChannel(channel, url)

        channels.filter { it.url == channel.url }
                .forEach { return }

        channels.add(channel)
        iMainPresenter.addChannelToMenu(channel, true)
        selectChannel(channel)
        iMainPresenter.createSnackbar("Канал добавлен: \"" + channel.titleChannel + "\"")
    }

    private fun setRefreshing(refresh: Boolean) {
        iMainPresenter.setRefreshing(refresh)

        if (refresh || activeChannelsUrl.size == 1) {
            iMainPresenter.enableItemToolbarDeleteChannel(!refresh)
        }
    }

    private fun findChannel(channel: Channel) {
        val result = async {
            mainModel.getItems(channel.id)
        }

        launch(UI) { insertElementsIntoView(result.await(), true) }

        selectChannel(channel)
    }

    private fun setActiveChannelsUrl(url: String = "") {
        activeChannelsUrl.clear()

        if (url == "") channels.forEach { activeChannelsUrl.add(it.url) }
        else activeChannelsUrl.add(url)

        if (activeChannelsUrl.size == 0) iMainPresenter.channelsNotFound()

        iMainPresenter.setActiveChannelsUrlIntoViewModel(activeChannelsUrl)
    }

    private fun getTitleByActiveChannelsUrlChannel(): String {
        if (activeChannelsUrl.size == 1) {
            channels.filter { it.url == activeChannelsUrl[0] }
                    .forEach { return it.titleChannel }
        }

        return "Все новости"
    }

    private fun requestItemsFromDb(setRefresh: Boolean) {
        val result = async {
            if (activeChannelsUrl.size == 1) {
                mainModel.getItems(getIdByUrlChannel(activeChannelsUrl[0]))
            } else {
                mainModel.getAllItems()
            }
        }

        launch(UI) { insertElementsIntoView(result.await(), setRefresh) }
    }

    private fun getIdByUrlChannel(url: String): Int {
        channels.filter { it.url == url }
                .forEach { return it.id }

        return 0
    }

    private fun channelInList(channel: Channel) {
        iMainPresenter.createSnackbar("Данный канал уже есть в списке")
        val result = async {
            mainModel.getItems(channel.id)
        }

        launch(UI) { insertElementsIntoView(result.await(), true) }

        selectChannel(channel)
    }

    private fun insertElementsIntoView(data: MutableList<RequestNewChannel>, setRefresh: Boolean) {
        updateCounter()

        iMainPresenter.insertElementsIntoView(data)
        if (setRefresh) setRefreshing(false)
    }

    private fun updateCounter() {
        activeChannelsUrl.forEach { requestSizeChannel(getIdByUrlChannel(it)) }

        requestSizeChannel(R.id.all_channels)
    }

    private fun updateAllCounter() {
        channels.forEach { requestSizeChannel(it.id) }

        requestSizeChannel(R.id.all_channels)
    }

    private fun requestSizeChannel(id: Int) {
        val result = async {
            if (id != R.id.all_channels) mainModel.getSizeChannel(id)
            else mainModel.getSizeChannel()
        }

        launch(UI) { iMainPresenter.updateCounter(id, result.await().toString()) }
    }

    private fun selectChannel(channel: Channel) {
        setActiveChannelsUrl(channel.url)

        with(iMainPresenter) {
            goToTop(true)

            setTitleToolbar(channel.titleChannel)
            checkMenuItem(channel.getMenuId())
        }
    }

    private fun goToTop(enable: Boolean) {
        with(iMainPresenter) {
            scrollToPosition(0)
            showToolbar()
            enableItemToolbarDeleteChannel(enable)
        }
    }

    override fun showException(t: Throwable, urlChannel: String) = iMainPresenter.showException(t, urlChannel)

}