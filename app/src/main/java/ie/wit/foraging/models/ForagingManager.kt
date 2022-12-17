//package ie.wit.foraging.models
//
//import androidx.lifecycle.MutableLiveData
//import com.google.firebase.auth.FirebaseUser
//import timber.log.Timber.i
//
//var lastId = 0L
//
//internal fun getId(): Long {
//    return lastId++
//}
//
//object ForagingManager : ForagingStore {
//
//    val foragingList = ArrayList<ForagingModel>()
////    val userList = ArrayList<UserModel>()
//
////    override fun findAll(): List<ForagingModel> {
////        return foragingList
////    }
//
////    override fun findAllUsers(): List<UserModel> {
////        return userList
////    }
//
////    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, foraging: ForagingModel) {
////        foraging.uid = getId()
////        foragingList.add(foraging)
////        logAll()
////    }
//
////    override fun createUser(user: UserModel) {
////        user.id = getId()
////        userList.add(user)
////        logAll()
////    }
//
//    override fun update(foraging: ForagingModel) {
//        val foundForaging: ForagingModel? = foragingList.find { p -> p.id == foraging.id }
//        if (foundForaging != null) {
//            foundForaging.commonPlantName = foraging.commonPlantName
//            foundForaging.scientificPlantName = foraging.scientificPlantName
//            foundForaging.datePlantPicked = foraging.datePlantPicked
//            foundForaging.image = foraging.image
////            foundForaging.lat = foraging.lat
////            foundForaging.lng = foraging.lng
////            foundForaging.zoom = foraging.zoom
//            logAll()
//        }
//    }
//
//    fun logAll() {
//        foragingList.forEach { i("${it}") }
//    }
//
//    override fun delete(foraging: ForagingModel) {
//        foragingList.remove(foraging)
//    }
//}