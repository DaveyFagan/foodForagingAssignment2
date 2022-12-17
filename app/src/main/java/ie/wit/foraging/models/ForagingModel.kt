package ie.wit.foraging.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ForagingModel(
    var uid: String? = "N/A",
    var commonPlantName: String = "N/A",
    var scientificPlantName: String = "N/A",
    var datePlantPicked: String = "N/A",
    var image: String = "N/A",
//    val message: String = "N/A",
//    val upvotes: Int = 0,
    var email: String? = "eric@cartman.com"
//    var lat: Double = 0.0,
//    var lng: Double = 0.0,
//    var zoom: Float = 0f
) : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "commonPlantName" to commonPlantName,
            "scientificPlantName" to scientificPlantName,
            "datePlantPicked" to datePlantPicked,
            "image" to image,
//            "message" to message,
//            "upvotes" to upvotes,
            "email" to email
        )
    }
}