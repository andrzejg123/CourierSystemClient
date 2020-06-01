package pl.polsl.couriersystemclient.datasource

import android.util.Log
import com.google.gson.JsonObject
import pl.polsl.couriersystemclient.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataSource {
    private var retrofit: Retrofit
    private var api: CourierSystemService

    init {
        retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.6:8080/courier-system/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        api = retrofit.create(CourierSystemService::class.java)
    }

    fun getCars(callback: RequestReadyCallback) {
        api.getCars().enqueue(object: Callback<List<Car>> {
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Log.e("error", t.message!!)
            }

            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                callback.onRequestReady("getCars", response.body() ?: emptyList<Car>())
            }
        })
    }

    fun patchCar(car: Car, callback: RequestReadyCallback) {
        api.patchCar(car.id, CarPatch(
            car.name,
            car.inUse,
            car.latLng
        )).enqueue(object: Callback<Car> {

            override fun onFailure(call: Call<Car>, t: Throwable) {
                Log.e("error", t.message!!)
            }

            override fun onResponse(call: Call<Car>, response: Response<Car>) {
                callback.onRequestReady("patchCar", response.body() ?: response.raw())
            }

        })
    }

    fun getPackages(callback: RequestReadyCallback) {
        api.getPackages().enqueue(object: Callback<List<PackageGet>> {
            override fun onFailure(call: Call<List<PackageGet>>, t: Throwable) {
                Log.e("error", t.message!!)
            }

            override fun onResponse(call: Call<List<PackageGet>>, response: Response<List<PackageGet>>) {
                val packages = response.body()
                callback.onRequestReady("getPackages", packages)
            }

        })
    }

    fun getPlaces(callback: RequestReadyCallback) {
        api.getPlaces().enqueue(object: Callback<List<PlaceGet>> {
            override fun onFailure(call: Call<List<PlaceGet>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<PlaceGet>>, response: Response<List<PlaceGet>>) {
                callback.onRequestReady("getPlaces", response.body())
            }

        })
    }

    fun getRoutes(callback: RequestReadyCallback) {
        api.getRoutes().enqueue(object: Callback<List<RouteGet>> {
            override fun onFailure(call: Call<List<RouteGet>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<RouteGet>>, response: Response<List<RouteGet>>) {
                callback.onRequestReady("getRoutes", response.body())
            }

        })
    }

    fun getClients(callback: RequestReadyCallback) {
        api.getClients().enqueue(object: Callback<List<ClientGet>> {
            override fun onFailure(call: Call<List<ClientGet>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<ClientGet>>, response: Response<List<ClientGet>>) {
                Log.e("csss", response.body()!!.size.toString())
                callback.onRequestReady("getClients", response.body())
            }

        })
    }

    fun startPackageDelivery(p: Package, callback: RequestReadyCallback) {

        api.startPackageDelivery(p.id,
            JsonBodyHelper.getJsonBody(Pair("carId", p.carId!!)))
            .enqueue(object: Callback<PackageGet> {
                override fun onFailure(call: Call<PackageGet>, t: Throwable) {
                    Log.e("coss", t.message!!)
                }

                override fun onResponse(call: Call<PackageGet>, response: Response<PackageGet>) {
                    callback.onRequestReady("startPackageDelivery", response.body())
                }

            })
    }

    fun finishPackageDelivery(p: Package, callback: RequestReadyCallback) {
        api.deliverPackage(p.id).enqueue(object: Callback<PackageGet> {
            override fun onFailure(call: Call<PackageGet>, t: Throwable) {
            }

            override fun onResponse(call: Call<PackageGet>, response: Response<PackageGet>) {
                callback.onRequestReady("finishPackageDelivery", response.body())
            }

        })
    }
}