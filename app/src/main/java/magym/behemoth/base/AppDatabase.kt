package magym.behemoth.base

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import magym.behemoth.model.Channel
import magym.behemoth.model.New

@Database(entities = [(New::class), (Channel::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daoNew(): DaoNew

    abstract fun daoChannel(): DaoChannel

}