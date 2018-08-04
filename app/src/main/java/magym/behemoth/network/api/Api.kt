package magym.rssreader.network.api

import io.reactivex.Single
import magym.rssreader.model.Rss
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET
    fun getRssData(@Url url: String): Single<Rss>

}