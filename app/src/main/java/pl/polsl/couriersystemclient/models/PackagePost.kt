package pl.polsl.couriersystemclient.models

data class PackagePost(
    val name: String,
    val clientId: Long,
    val routeId: Long
)