package ie.wit.foraging.ui.foragingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.foraging.firebase.FirebaseDBManager
import ie.wit.foraging.models.ForagingModel
import timber.log.Timber

class ForagingListViewModel : ViewModel() {

    private val foragingList = MutableLiveData<List<ForagingModel>>()

    val observableForagingList: LiveData<List<ForagingModel>>
        get() = foragingList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
//            ForagingManager.findAll(liveFirebaseUser.value?.email!!, foragingList)
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,foragingList)
            Timber.i("ForagingList Load Success : ${foragingList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("ForagingList Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //ForagingManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

}