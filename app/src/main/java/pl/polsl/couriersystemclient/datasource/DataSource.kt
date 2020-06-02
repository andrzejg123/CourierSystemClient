package pl.polsl.couriersystemclient.datasource

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import pl.polsl.couriersystemclient.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataSource {
    private var retrofit: Retrofit
    private var api: CourierSystemService

    init {
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method(), original.body())
                .build()
            Log.d("halo", "url = ${request.url().url()}")
            chain.proceed(request)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.41:8080/courier-system/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
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
                    Log.e("coss", t.message.toString())
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

    fun createPackage(packagePost: PackagePost, callback: RequestReadyCallback) {
        api.createPackage(packagePost).enqueue(object: Callback<PackageGet> {
            override fun onFailure(call: Call<PackageGet>, t: Throwable) {
            }

            override fun onResponse(call: Call<PackageGet>, response: Response<PackageGet>) {
                callback.onRequestReady("createPackage", response.body())
            }

        })
    }

    fun createClient(clientPost: ClientPost, callback: RequestReadyCallback) {
        api.createClient(clientPost).enqueue(object: Callback<ClientGet> {
            override fun onFailure(call: Call<ClientGet>, t: Throwable) {
            }

            override fun onResponse(call: Call<ClientGet>, response: Response<ClientGet>) {
                callback.onRequestReady("createClient", response.body())
            }

        })
    }

}