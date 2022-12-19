package ie.wit.foraging.ui.foraging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.foraging.firebase.FirebaseDBManager
import ie.wit.foraging.models.ForagingModel

class ForagingViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    private val foraging = MutableLiveData<ForagingModel>()

    val observableStatus: LiveData<Boolean>
        get() = status

    val observableForaging: LiveData<ForagingModel>
        get() = foraging

    fun addForaging(firebaseUser: MutableLiveData<FirebaseUser>, foraging: ForagingModel) {
        status.value = try {
//            ForagingManager.create(foraging)
            FirebaseDBManager.create(firebaseUser, foraging)

            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}