package pl.polsl.couriersystemclient.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pl.polsl.couriersystemclient.R
import pl.polsl.couriersystemclient.models.Car
import pl.polsl.couriersystemclient.models.Package
import pl.polsl.couriersystemclient.packageinfo.PackageInfoActivity


class MapActivity : FragmentActivity(), OnMapReadyCallback, MapActivityCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    private lateinit var map: GoogleMap
    private var controller = MapActivityController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment!!.getMapAsync(this)

        controller.init()
        controller.selectedCar = intent.getSerializableExtra("selectedCar") as Car

    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.e("map", "ready")
        map = p0!!
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(this)
    }

    override fun printMarkers(packages: List<Package>) {
        packages.forEach {
            val marker = map.addMarker(MarkerOptions()
                .position(
                    LatLng(it.route.startPlace.latLng.latitude, it.route.startPlace.latLng.longitude)
                ).title(it.name).also { it2 ->
                    if (it.startOfDeliveryDate != null) {
                        it2.icon(BitmapDescriptorFactory.defaultMarker(300.0f))
                        controller.selectedPackage = it
                    }
                }
            )
            controller.placesWithMarkers[marker] = it
        }
    }

    override fun printDestinationMarker(p: Package) {
        controller.destinationMarker = map.addMarker(MarkerOptions()
            .position(LatLng(p.route.endPlace.latLng.latitude, p.route.endPlace.latLng.longitude)).title("Destination " + p.name).icon(
                BitmapDescriptorFactory.defaultMarker(120.0f)).zIndex(1.0f)
        )
    }

    override fun routeToPackageInfo() {
        val intent = Intent(this, PackageInfoActivity::class.java)
        intent.putExtra("selectedPackage", controller.selectedPackage)
        startActivityForResult(intent, 1)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        controller.unselectCar()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        controller.onMarkerClick(p0)
        return false
    }

    override fun onMapClick(p0: LatLng?) {
        if(!controller.isPackageInDelivery || (controller.isPackageInDelivery && controller.isPackageDelivered))
            controller.destinationMarker?.remove()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                controller.onResultOk(data?.getSerializableExtra("selectedPackage") as Package)
            }
        }
    }
}
