package pl.polsl.couriersystemclient.packageinfo

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_package_info.*
import pl.polsl.couriersystemclient.R
import pl.polsl.couriersystemclient.models.Package

class PackageInfoActivity : AppCompatActivity(), View.OnClickListener, PackageInfoActivityCallback {

    private val controller = PackageInfoActivityController(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_info)

        val selectedPackage: Package = intent.getSerializableExtra("selectedPackage") as Package
        controller.init(selectedPackage)
        if(controller.isPackageInDelivery)
            deliver_button.text = getString(R.string.finish_delivery_button)

        text_view_package_info.text = """${controller.selectedPackage.name},
Client: ${selectedPackage.client.name}, ${selectedPackage.client.surname}
Phone number: ${selectedPackage.client.phoneNumber}
Email: ${selectedPackage.client.email}

Destination: 
Lat: ${selectedPackage.route.endPlace.latLng.latitude}
Lon: ${selectedPackage.route.endPlace.latLng.longitude}"""

        if(controller.isPackageDelivered) {
            text_view_package_info.text = "\n--Delivered--\n\n" + text_view_package_info.text
            deliver_button.isEnabled = false
        }

        deliver_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(controller.isPackageInDelivery) {
            controller.onFinishDeliverClick()
        }
        else {
            controller.onDeliverClick()
            deliver_button.text = getString(R.string.finish_delivery_button)
        }

    }

    override fun returnWithDeliveryButton() {
        val returnIntent = intent
        returnIntent.putExtra("selectedPackage", controller.selectedPackage)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = intent
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }
}
