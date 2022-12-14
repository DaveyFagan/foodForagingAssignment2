package ie.wit.foraging.ui.foraging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.foraging.models.ForagingManager
import ie.wit.foraging.models.ForagingModel

class ForagingViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addForaging(foraging: ForagingModel) {
        status.value = try {
            ForagingManager.create(foraging)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}