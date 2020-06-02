package pl.polsl.couriersystemclient.datasource

import com.google.gson.JsonObject
import pl.polsl.couriersystemclient.models.*
import retrofit2.Call
import retrofit2.http.*

interface CourierSystemService {

    @GET("car")
    fun getCars(): Call<List<Car>>

    @PATCH("car/{id}")
    fun patchCar(@Path("id") id: Long, @Body car: CarPatch): Call<Car>

    @GET("package")
    fun getPackages(): Call<List<PackageGet>>

    @GET("place")
    fun getPlaces(): Call<List<PlaceGet>>

    @GET("route")
    fun getRoutes(): Call<List<RouteGet>>

    @GET("client")
    fun getClients(): Call<List<ClientGet>>

    @PATCH("package/start_delivery/{id}")
    fun startPackageDelivery(@Path("id") id: Long, @Body body: JsonObject): Call<PackageGet>

    @PATCH("package/deliver/{id}")
    fun deliverPackage(@Path("id") id: Long): Call<PackageGet>

    @POST("package")
    fun createPackage(@Body packagePost: PackagePost): Call<PackageGet>

    @POST("client")
    fun createClient(@Body clientPost: ClientPost): Call<ClientGet>

}