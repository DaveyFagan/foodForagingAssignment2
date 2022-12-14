package ie.wit.foraging.ui.foragingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.foraging.models.ForagingManager
import ie.wit.foraging.models.ForagingModel

class ForagingListViewModel : ViewModel() {

    private val foragingList = MutableLiveData<List<ForagingModel>>()

    val observableDonationsList: LiveData<List<ForagingModel>>
        get() = foragingList

    init {
        load()
    }

    fun load() {
        foragingList.value = ForagingManager.findAll()
    }
}