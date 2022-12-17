package ie.wit.foraging.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.foraging.firebase.FirebaseDBManager
import ie.wit.foraging.models.ForagingModel
import timber.log.Timber

class PlantDetailViewModel : ViewModel() {

    private val foraging = MutableLiveData<ForagingModel>()

    var observablePlant: LiveData<ForagingModel>
        get() = foraging
        set(value) {foraging.value = value.value}

    fun getPlant(userid:String, id: String) {
        try {
            //ForagingManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, foraging)
            Timber.i("Detail getPlant() Success : ${
                foraging.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getPlant() Error : $e.message")
        }
    }

    fun updatePlant(userid:String, id: String,foraging: ForagingModel) {
        try {
            //ForagingManager.update(email, id, donation)
            FirebaseDBManager.update(userid, id, foraging)
            Timber.i("Detail update() Success : $foraging")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}