package pl.polsl.couriersystemclient.map

import pl.polsl.couriersystemclient.models.*

class DataHolder {
    var packages: ArrayList<Package> = ArrayList()
    var routes: ArrayList<Route> = ArrayList()
    var places: List<PlaceGet> = emptyList()
    var clients: List<ClientGet> = emptyList()

    fun prepareRoutes(routesGet: List<RouteGet>) {
        routesGet.forEach {
            routes.add(Route(it.id,
                places.find {it2 ->
                    it2.id == it.startPlaceId
                }!!,
                places.find {it2 ->
                    it2.id == it.endPlaceId
                }!!
            ))
        }
    }

    fun preparePackages(packagesGet: List<PackageGet>, selectedCarId: Long) {
        packagesGet.filter {
            it.deliveryDate == null && (if (it.startOfDeliveryDate != null) it.carId == selectedCarId else true)
        }.forEach {
            packages.add(Package(
                it.id, it.name,
                clients.find { it2 ->
                    it.clientId == it2.id
                }!!,
                it.carId,
                routes.find { it2 ->
                    it.routeId == it2.id
                }!!,
                it.registerDate, it.startOfDeliveryDate, it.deliveryDate
            ))
        }
    }
}