package pl.polsl.couriersystemclient.packageinfo

import pl.polsl.couriersystemclient.datasource.DataSource
import pl.polsl.couriersystemclient.datasource.RequestReadyCallback
import pl.polsl.couriersystemclient.models.Package
import pl.polsl.couriersystemclient.models.PackageGet
import java.util.*

class PackageInfoActivityController(private val activityCallback: PackageInfoActivityCallback): RequestReadyCallback {
    lateinit var selectedPackage: Package
    val isPackageInDelivery: Boolean
        get() = selectedPackage.startOfDeliveryDate != null

    val isPackageDelivered: Boolean
        get() = selectedPackage.deliveryDate != null

    fun init(p: Package) {
        selectedPackage = p
    }

    fun onDeliverClick() {
        selectedPackage.startOfDeliveryDate = Date()
        DataSource.startPackageDelivery(selectedPackage, this)
    }

    fun onFinishDeliverClick() {
        selectedPackage.deliveryDate = Date()
        DataSource.finishPackageDelivery(selectedPackage, this)
    }

    override fun onRequestReady(message: String, data: Any?) {
        when(message) {
            "startPackageDelivery" -> {
                val packageGet = data as PackageGet
                selectedPackage = Package(
                    packageGet.id, packageGet.name, selectedPackage.client, packageGet.carId,
                    selectedPackage.route, packageGet.registerDate, packageGet.startOfDeliveryDate, null
                )
                activityCallback.returnWithDeliveryButton()
            }

            "finishPackageDelivery" -> {
                val packageGet = data as PackageGet
                selectedPackage = Package(
                    packageGet.id, packageGet.name, selectedPackage.client, packageGet.carId,
                    selectedPackage.route, packageGet.registerDate, packageGet.startOfDeliveryDate, packageGet.deliveryDate
                )
                activityCallback.returnWithDeliveryButton()
            }
        }
    }


}