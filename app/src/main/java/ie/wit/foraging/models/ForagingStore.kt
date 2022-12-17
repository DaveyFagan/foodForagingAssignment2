package ie.wit.foraging.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface ForagingStore {
    fun findAll(
        foragingList:
        MutableLiveData<List<ForagingModel>>
    )

    fun findAll(
        userid: String,
        foragingList:
        MutableLiveData<List<ForagingModel>>
    )

    fun findById(
        userid: String, foragingid: String,
        foraging: MutableLiveData<ForagingModel>
    )

    fun create(firebaseUser: MutableLiveData<FirebaseUser>, foraging: ForagingModel)
    fun delete(userid: String, foragingid: String)
    fun update(userid: String, foragingid: String, foraging: ForagingModel)

}