package pl.polsl.couriersystemclient.models

import java.io.Serializable

data class PlaceGet(
    var id: Long,
    var latLng: LatLng,
    var name: String
) : Serializable