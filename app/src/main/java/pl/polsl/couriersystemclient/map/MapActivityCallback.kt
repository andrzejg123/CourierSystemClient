package pl.polsl.couriersystemclient.map
import pl.polsl.couriersystemclient.models.Package

interface MapActivityCallback {
    fun printMarkers(packages: List<Package>)

    fun printDestinationMarker(p: Package)

    fun routeToPackageInfo()
}