package pl.polsl.couriersystemclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LatLng(
    //@SerializedName("latitude")
    //@Expose
    var latitude: Double,

    //@SerializedName("longitude")
    //@Expose
    var longitude: Double
) : Serializable