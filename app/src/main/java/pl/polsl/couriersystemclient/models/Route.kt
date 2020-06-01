package pl.polsl.couriersystemclient.models

import java.io.Serializable

data class Route (
    var id: Long,
    var startPlace: PlaceGet,
    var endPlace: PlaceGet
) : Serializable