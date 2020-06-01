package pl.polsl.couriersystemclient.models

import java.io.Serializable
import java.util.*

data class Package(
    var id: Long,
    var name: String,
    var client: ClientGet,
    var carId: Long?,
    var route: Route,
    var registerDate: Date,
    var startOfDeliveryDate: Date?,
    var deliveryDate: Date?
) : Serializable