package ie.wit.foraging.ui.foraging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.foraging.firebase.FirebaseDBManager
import ie.wit.foraging.models.ForagingModel
import timber.log.Timber

class ForagingViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addForaging(firebaseUser: MutableLiveData<FirebaseUser>, foraging: ForagingModel) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser, foraging)

            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}