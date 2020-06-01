package pl.polsl.couriersystemclient

import pl.polsl.couriersystemclient.models.Car

interface MainActivityCallback {
    fun notifyRecyclerView(data: List<Car>)

    fun routeToMap()
}