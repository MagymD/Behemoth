package magym.rssreader.main

interface IOnClick {

    fun openChannel(idChannel: Int)

    fun openBrowser(link: String)

    fun openPhoto(url: String)

}