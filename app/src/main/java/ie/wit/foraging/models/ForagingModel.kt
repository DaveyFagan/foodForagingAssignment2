package ie.wit.foraging.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForagingModel(
    var id: Long = 0,
    var commonPlantName: String = "",
    var scientificPlantName: String = "",
    var datePlantPicked: String = "",
    var image: Uri = Uri.EMPTY,
//    var lat: Double = 0.0,
//    var lng: Double = 0.0,
//    var zoom: Float = 0f

) : Parcelable