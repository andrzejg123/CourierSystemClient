package pl.polsl.couriersystemclient

import pl.polsl.couriersystemclient.datasource.RequestReadyCallback
import pl.polsl.couriersystemclient.datasource.DataSource
import pl.polsl.couriersystemclient.models.Car

class MainActivityController(private val activityCallback: MainActivityCallback): RequestReadyCallback {
    var cars: List<Car> = emptyList()
    lateinit var selectedCar: Car

    fun init() {
        DataSource.getCars(this)
    }

    override fun onRequestReady(message: String, data: Any?) {
        when(message) {
            "getCars" -> {
                cars = data as List<Car>
                activityCallback.notifyRecyclerView(data)
            }

            "patchCar" -> {
                activityCallback.routeToMap()
            }
        }

    }

    fun selectCar(position: Int) {
        selectedCar = cars[position]
        selectedCar.inUse = true
        DataSource.patchCar(selectedCar, this)
    }

}