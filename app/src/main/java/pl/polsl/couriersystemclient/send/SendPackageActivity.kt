package pl.polsl.couriersystemclient.send

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_send_package.*
import pl.polsl.couriersystemclient.R
import pl.polsl.couriersystemclient.datasource.DataSource
import pl.polsl.couriersystemclient.datasource.RequestReadyCallback
import pl.polsl.couriersystemclient.models.*


class SendPackageActivity: AppCompatActivity(), View.OnClickListener, RequestReadyCallback {

    private val users = hashSetOf<ClientGet>()
    private val routes = arrayListOf<RouteGet>()
    private val places = hashMapOf<Long, PlaceGet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_package)
        setSupportActionBar(toolbar)
        title = "Client panel"
        DataSource.getClients(this)
        DataSource.getPlaces(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.sendPackagesButton -> sendPackage()
            R.id.showPackagesButton -> DataSource.getPackages(this)
            R.id.createUserButton -> createClient()
        }
    }

    private fun createClient() {
        val name = createNameEditText.text?.toString()?.nonBlank
        val surname = createSurnameEditText.text?.toString()?.nonBlank
        val email = createEmailEditText.text?.toString()?.nonBlank
        val phoneNumber = createPhoneNumberEditText.text?.toString()?.nonBlank
        if(users.any { it.phoneNumber == phoneNumber })
            return Toast.makeText(this, "Phone number already used", Toast.LENGTH_SHORT).show()
        if(name != null && surname != null && phoneNumber != null) {
            DataSource.createClient(ClientPost(name, surname, email, phoneNumber), this)
        } else {
            Toast.makeText(this, "Check entered data!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPackages(packages: List<PackageGet>) {
        val phoneNumber = clientPhoneNumberEditText.text?.toString()?.nonBlank
        val client = users.find { it.phoneNumber == phoneNumber }
        if(client == null) {
            Toast.makeText(this, "Wrong phone number", Toast.LENGTH_SHORT).show()
            return
        }
        val list = packages.filter { it.clientId == client.id }.map { packageGet ->
            routes.find { packageGet.routeId == it.id }?.let {
                "${packageGet.name} (${places[it.startPlaceId]?.name} - ${places[it.endPlaceId]?.name})"
            }.toString()
        }

        AlertDialog.Builder(this)
            .setTitle("Your pacakges")
            .setItems(list.toTypedArray()) { _, _ -> }
            .setNegativeButton("cancel") { _, _ -> }
            .show()
    }

    private fun sendPackage(){
        val packageName = packageNameEditText.text?.toString()?.nonBlank
        val phoneNumber = clientPhoneNumberEditText.text?.toString()?.nonBlank
        val client = users.find { it.phoneNumber == phoneNumber }
        val route = routes.getOrNull(routeSpinner.selectedItemId.toInt())
        if(packageName != null && client != null && route != null) {
            DataSource.createPackage(PackagePost(packageName, client.id, route.id), this)
        } else {
            Toast.makeText(this, "Wrong phone number or empty package name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestReady(message: String, data: Any?) {
        when(message) {
            "getPlaces" -> {
                (data as? List<PlaceGet> ?: emptyList()).forEach {
                    places[it.id] = it
                }
                DataSource.getRoutes(this)
            }
            "getClients" -> users.addAll(data as? List<ClientGet> ?: emptyList())
            "getRoutes" -> {
                routes.addAll(data as? List<RouteGet> ?: emptyList())
                setupSpinner()
            }
            "createPackage" -> Toast.makeText(this, "Package sent", Toast.LENGTH_SHORT).show()
            "getPackages" -> showPackages(data as? List<PackageGet> ?: emptyList())
            "createClient" -> {
                val client = data as? ClientGet
                if(client != null) {
                    users.add(client)
                    Toast.makeText(this, "Client created", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Failed to create client", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner() {
        routeSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            routes.map { "${places[it.startPlaceId]?.name} - ${places[it.endPlaceId]?.name}" }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private val String.nonBlank
        get() = if(isNotBlank()) trim() else null

}