package magym.rssreader.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import magym.rssreader.model.New
import magym.rssreader.model.RequestNewChannel

@Dao
interface DaoNew {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertElements(news: List<New>)

    @Query("""SELECT New.*, Channel.url, Channel.urlIcoChannel, Channel.titleChannel FROM New JOIN Channel
            ON New.idChannel = Channel.id ORDER BY dateLong DESC""")
    fun getAllNewsChannel(): MutableList<RequestNewChannel>

    @Query("""SELECT New.*, Channel.url, Channel.urlIcoChannel, Channel.titleChannel FROM New JOIN Channel
            ON New.idChannel = :idChannel AND Channel.id = :idChannel ORDER BY dateLong DESC""")
    fun getNewsChannel(idChannel: Int): MutableList<RequestNewChannel>

    @Query("SELECT COUNT(id) FROM New WHERE idChannel = :idChannel")
    fun getSizeChannel(idChannel: Int): Int

    @Query("SELECT COUNT(id) FROM New")
    fun getSizeChannel(): Int

    @Query("DELETE from New WHERE idChannel IN (:idChannel)")
    fun deleteElements(idChannel: Int)

}