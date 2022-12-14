package ie.wit.foraging.main

import android.app.Application
import ie.wit.foraging.models.ForagingStore
import timber.log.Timber

class ForagingApp : Application() {

    lateinit var foragingStore: ForagingStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        foragingStore = ForagingMemStore()
        Timber.i("Starting Foraging Application")
    }
}