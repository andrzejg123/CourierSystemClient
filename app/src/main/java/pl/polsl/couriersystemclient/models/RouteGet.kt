package pl.polsl.couriersystemclient.models

data class RouteGet(
    var id: Long,
    var startPlaceId: Long,
    var endPlaceId: Long
)