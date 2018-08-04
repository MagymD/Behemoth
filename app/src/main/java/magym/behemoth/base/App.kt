package magym.rssreader.base

import android.app.Application
import android.arch.persistence.room.Room
import com.facebook.stetho.Stetho

class App : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration() // Attention
                .allowMainThreadQueries() // Attention
                .build()
    }

    companion object {

        lateinit var instance: App

        private val sLock = Any()

    }

    override fun onCreate() {
        super.onCreate()
        synchronized(sLock) { instance = this }

        //Stetho()
    }

    private fun Stetho() {
        // Create an InitializerBuilder
        val initializerBuilder = Stetho.newInitializerBuilder(this)

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        )

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        )

        // Use the InitializerBuilder to generate an Initializer
        val initializer = initializerBuilder.build()

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer)
    }

}