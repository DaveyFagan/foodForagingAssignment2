package ie.wit.foraging.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.foraging.models.ForagingModel
import ie.wit.foraging.models.ForagingStore
import timber.log.Timber

object FirebaseDBManager : ForagingStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(foragingList: MutableLiveData<List<ForagingModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, foragingList: MutableLiveData<List<ForagingModel>>) {
        database.child("user-foraginglist").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase ForagingList error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ForagingModel>()
                    val children = snapshot.children
                    children.forEach {
                        val foraging = it.getValue(ForagingModel::class.java)
                        localList.add(foraging!!)
                    }
                    database.child("user-foraginglist").child(userid)
                        .removeEventListener(this)

                    foragingList.value = localList
                }
            })
    }

    override fun findById(userid: String, foragingid: String, foraging: MutableLiveData<ForagingModel>) {

        database.child("user-foraginglist").child(userid)
            .child(foragingid).get().addOnSuccessListener {
                foraging.value = it.getValue(ForagingModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, foraging: ForagingModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("foraginglist").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        foraging.uid = key
        val foragingValues = foraging.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/foraginglist/$key"] = foragingValues
        childAdd["/user-foraginglist/$uid/$key"] = foragingValues
        Timber.i("Firebase Add: $foragingValues Firebase Key: $key")
        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, foragingid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/foraginglist/$foragingid"] = null
        childDelete["/user-foraginglist/$userid/$foragingid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, foragingid: String, foraging: ForagingModel) {

        val foragingValues = foraging.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["foraginglist/$foragingid"] = foragingValues
        childUpdate["user-foraginglist/$userid/$foragingid"] = foragingValues

        Timber.i("Plant Detail update : $childUpdate")

        database.updateChildren(childUpdate)
    }

}