package pl.polsl.couriersystemclient.models

import java.io.Serializable

data class Car (

    var id: Long,

    //@SerializedName("name")
    //@Expose
    var name: String,

    //@SerializedName("inUse")
   // @Expose
    var inUse: Boolean,

    //@SerializedName("latLng")
    //@Expose
    var latLng: LatLng
) : Serializable