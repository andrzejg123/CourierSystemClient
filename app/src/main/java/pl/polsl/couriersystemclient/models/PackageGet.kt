package pl.polsl.couriersystemclient.models

import java.util.*

data class PackageGet(
    var id: Long,
    var name: String,
    var clientId: Long,
    var carId: Long?,
    var routeId: Long,
    var registerDate: Date,
    var startOfDeliveryDate: Date?,
    var deliveryDate: Date?
)