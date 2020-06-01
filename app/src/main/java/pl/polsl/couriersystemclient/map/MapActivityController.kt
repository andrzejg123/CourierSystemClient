package pl.polsl.couriersystemclient.map

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import pl.polsl.couriersystemclient.datasource.DataSource
import pl.polsl.couriersystemclient.datasource.RequestReadyCallback
import pl.polsl.couriersystemclient.models.*

class MapActivityController(private val activityCallback: MapActivityCallback): RequestReadyCallback {

    lateinit var selectedCar: Car
    var selectedPackage: Package? = null
    val dataHolder = DataHolder()
    var placesWithMarkers: HashMap<Marker, Package> = HashMap()
    var destinationMarker: Marker? = null
    val isPackageInDelivery
        get() = (selectedPackage?.startOfDeliveryDate != null)
    val isPackageDelivered: Boolean
        get() = selectedPackage?.deliveryDate != null


    fun init() {
        DataSource.getPlaces(this)
    }

    override fun onRequestReady(message: String, data: Any?) {
        when(message) {
            "getPackages" -> {
                val packagesGet = data as List<PackageGet>
                dataHolder.preparePackages(packagesGet, selectedCar.id)
                activityCallback.printMarkers(dataHolder.packages)
            }

            "getPlaces" -> {
                dataHolder.places = data as List<PlaceGet>
                DataSource.getRoutes(this)
            }

            "getRoutes" -> {
                val routesGet = data as List<RouteGet>
                dataHolder.prepareRoutes(routesGet)
                DataSource.getClients(this)
            }

            "getClients" -> {
                dataHolder.clients = data as List<ClientGet>
                DataSource.getPackages(this)
            }
        }
    }

    fun unselectCar() {
        selectedCar.inUse = false
        DataSource.patchCar(selectedCar, this)
    }

    fun onMarkerClick(marker: Marker?) {
        if (marker == destinationMarker) {
            selectedPackage?.carId = selectedCar.id
            activityCallback.routeToPackageInfo()

        } else if(!isPackageInDelivery) {
            destinationMarker?.remove()
            selectedPackage = placesWithMarkers[marker]!!
            activityCallback.printDestinationMarker(selectedPackage!!)

        } else if(isPackageDelivered && placesWithMarkers[marker] == selectedPackage) {
            activityCallback.routeToPackageInfo() //now package is delivered
        }

    }

    fun onResultOk(p: Package) {
        val selectedPackageOld = selectedPackage //package before update from second activity
        selectedPackage = p
        if(isPackageDelivered) {
            val marker = placesWithMarkers.filterValues {
                it == selectedPackageOld
            }.keys.first()

            marker.setIcon(BitmapDescriptorFactory.defaultMarker(270.0f))
            placesWithMarkers[marker] = p //update package in map
        }
        else if(isPackageInDelivery) {
            val marker = placesWithMarkers.filterValues {
                it == selectedPackageOld
            }.keys.first()

            placesWithMarkers[marker] = p //update package in map
        }
    }
}