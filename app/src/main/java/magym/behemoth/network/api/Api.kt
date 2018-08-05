package magym.behemoth.network.api

import io.reactivex.Single
import magym.behemoth.model.Rss
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET
    fun getRssData(@Url url: String): Single<Rss>

}