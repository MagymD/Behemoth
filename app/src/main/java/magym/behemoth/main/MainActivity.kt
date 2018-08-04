package magym.rssreader.main

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_app_bar.*
import kotlinx.android.synthetic.main.main_content.*
import magym.rssreader.R
import magym.rssreader.image.PhotoActivity
import magym.rssreader.main.recyclerview.ItemAdapter
import magym.rssreader.model.Channel
import magym.rssreader.model.RequestNewChannel
import magym.rssreader.utils.createTextView
import magym.rssreader.utils.getMenuId
import magym.rssreader.utils.loadMenuIco
import magym.rssreader.utils.showSnackbar
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException
import javax.xml.stream.XMLStreamException

class MainActivity : AppCompatActivity(),
        SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener,
        IOnClick, IMainView {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var toolbarMenu: Menu
    private lateinit var navigationMenu: Menu

    private var isOpenLink = false
    private var input: EditText? = null

    private val news: ArrayList<RequestNewChannel> = ArrayList()

    private val mainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // ToolBar
        setSupportActionBar(toolbar)
        toolbarMenu = toolbar.menu

        // SwipeRefreshLayout
        main_swipe_refresh_layout.setOnRefreshListener(this)
        main_swipe_refresh_layout.setColorSchemeResources(android.R.color.holo_blue_dark)

        // NavigationView & DrawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        navigation_view.setNavigationItemSelectedListener(this)
        navigation_view.itemIconTintList = null
        navigationMenu = navigation_view.menu
        navigationMenu.findItem(R.id.all_channels).actionView = this.createTextView()

        // RecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this) /*as RecyclerView.LayoutManager?*/
        recycler_view.adapter = ItemAdapter(news, this)

        // MainViewModel & Items
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainPresenter.setViewModel(mainViewModel)

        // NavigationMenu
        mainPresenter.addChannelsToMenu()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        if (isOpenLink) {
            setRefreshing(false)
            isOpenLink = false
        }
    }

    override fun onStop() {
        super.onStop()
        closeKeyboard()
    }

    // -------------------------------------------------------------------------------------- //

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_up -> {
                recycler_view.smoothScrollToPosition(0)
                showToolbar()
            }

            R.id.menu_refresh -> mainPresenter.requestData()
            R.id.menu_add_channel -> alertDialogNewChannel()
            R.id.menu_delete_channel -> alertDialogDeleteChannel()
        }

        return true
    }

    override fun setTitleToolbar(title: String) {
        toolbar.title = title
    }

    override fun showToolbar() = app_bar_layout.setExpanded(true)

    override fun enableItemToolbarDeleteChannel(enabled: Boolean) {
        toolbarMenu.findItem(R.id.menu_delete_channel)?.isEnabled = enabled
    }

    // -------------------------------------------------------------------------------------- //

    override fun onRefresh() = mainPresenter.requestData()

    override fun setRefreshing(refreshing: Boolean) {
        main_swipe_refresh_layout.isRefreshing = refreshing
    }

    // -------------------------------------------------------------------------------------- //

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_channel -> {
                alertDialogNewChannel()

                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.all_channels -> {
                mainPresenter.selectAllChannels()

                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
        }

        mainPresenter.findChannelByMenuItem(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun addChannelToMenu(channel: Channel, checked: Boolean) {
        val id = channel.getMenuId()

        if (navigationMenu.findItem(id) == null) {
            // FIXME: При добавлении вставка в конец, а сортировка по тайтлу. Исправить
            navigationMenu.add(R.id.group_channels, id, Menu.NONE, channel.titleChannel)
                    .setCheckable(true)
                    .setActionView(this.createTextView())
                    .isChecked = checked

            // Загружать картинку приходится позже, так как из-за Glide'а элементы могут вставиться в неправильном порядке
            navigationMenu.loadMenuIco(id, channel.urlIcoChannel, this)
        } else {
            navigationMenu.findItem(id)
                    .setVisible(true)
                    .isChecked = checked
        }

        enableItemToolbarDeleteChannel(true)
    }

    override fun deleteChannelFromMenu(id: Int) {
        navigationMenu.findItem(id)?.isVisible = false
    }

    override fun checkAllChannelsFromMenu() {
        navigationMenu.findItem(R.id.all_channels)?.isChecked = true
    }

    override fun checkMenuItem(id: Int) {
        navigationMenu.findItem(id)?.isChecked = true
    }

    override fun updateCounter(id: Int, text: String) {
        (navigationMenu.findItem(id)?.actionView as TextView).text = text
    }

    // -------------------------------------------------------------------------------------- //

    override fun openChannel(idChannel: Int) {
        mainPresenter.findChannelByIdChannel(idChannel)
    }


    override fun openBrowser(link: String) {
        setRefreshing(true)
        isOpenLink = true

        val openLinkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(openLinkIntent)
    }

    override fun openPhoto(url: String) {
        val fullScreenIntent = Intent(this, PhotoActivity::class.java)
        fullScreenIntent.putExtra("URL", url)
        startActivity(fullScreenIntent)
    }

    // -------------------------------------------------------------------------------------- //

    override fun updateDataSetChanged() {
        recycler_view.adapter.notifyDataSetChanged()
    }

    override fun scrollToPosition(position: Int) {
        recycler_view.scrollToPosition(position)
    }

    // -------------------------------------------------------------------------------------- //

    override fun insertElementsIntoView(newListNews: MutableList<RequestNewChannel>) {
        mainViewModel.news = newListNews

        news.clear()
        news.addAll(newListNews)
        updateDataSetChanged()
    }

    override fun showException(t: Throwable, urlChannel: String) {
        //this.log("", t)

        if (!hasConnection()) return

        when (t) {
            is IllegalArgumentException, is HttpException, is ConnectException,
            is SSLHandshakeException, is SSLProtocolException, is RuntimeException,
            is XMLStreamException,
            is UnknownHostException -> createSnackbarWithUrl("Неверно указан адрес канала", urlChannel)
            is SocketTimeoutException -> createSnackbarWithUrl("Время подключение истекло", urlChannel)
            else -> createSnackbar("Произошла ошибка сети")
        }
    }

    // -------------------------------------------------------------------------------------- //

    override fun createSnackbar(text: String) {
        recycler_view.showSnackbar(text)
    }

    private fun createSnackbarWithUrl(text: String, urlChannel: String) {
        recycler_view.showSnackbar("$text: \"$urlChannel\"")
    }

    override fun channelsNotFound() {
        createSnackbarWithButton("Ни одного канала не найдено, пожалуйста, добавьте каналы",
                "Добавить", ::alertDialogNewChannel)
    }

    private fun createSnackbarWithButton(text: String, buttonTitle: String, funAction: () -> Unit) {
        recycler_view.showSnackbar(text, buttonTitle, funAction)
    }

    // -------------------------------------------------------------------------------------- //

    private fun alertDialogNewChannel() {
        input = EditText(this)
        input!!.setText("http://") // TODO: Добавить: ввод текста на enter, символ '/'

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Добавить канал")
                .setMessage("Введите адрес rss канала")
                .setView(input)
                .setPositiveButton("Ввод") { _, _ ->
                    if (input!!.text.isNotEmpty()) {
                        mainPresenter.enterPositiveButton(input!!.text.toString())
                        closeKeyboard()
                    }
                }
                .setNegativeButton("Отмена") { _, _ -> closeKeyboard() }
                .setOnCancelListener { closeKeyboard() } // FIXME: иногда не закрывает при missclick'е
                .setOnDismissListener { closeKeyboard() }
        if (news.isEmpty())
            builder.setMessage("Введите адрес rss канала, например: \"http://lenta.ru/rss\"")
        builder.show()

        input!!.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun closeKeyboard() {
        input?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(input!!.windowToken, 0)
        }
    }

    private fun alertDialogDeleteChannel() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Удалить канал?")
                .setPositiveButton("Ок") { _, _ -> mainPresenter.deleteActiveChannel() }
                .setNegativeButton("Отмена") { _, _ -> }
        builder.show()
    }

    // -------------------------------------------------------------------------------------- //

    override fun updateItemsFromViewModel() {
        news.addAll(mainViewModel.news)
    }

    override fun setActiveChannelsUrlIntoViewModel(activeChannelsUrl: List<String>) {
        mainViewModel.activeChannelsUrl = activeChannelsUrl
    }

    // -------------------------------------------------------------------------------------- //

    override fun hasConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork == null) {
            createSnackbar("Нет связи с интернетом")
            return false
        }
        return true
    }

}