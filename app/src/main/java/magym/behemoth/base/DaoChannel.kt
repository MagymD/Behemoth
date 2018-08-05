package magym.behemoth.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import magym.behemoth.model.Channel

@Dao
interface DaoChannel {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertElement(channel: Channel)

    @Query("SELECT * FROM Channel ORDER BY titleChannel ASC")
    fun getAll(): List<Channel>

    @Query("DELETE from Channel WHERE id IN (:idChannel)")
    fun deleteElement(idChannel: Int)

}