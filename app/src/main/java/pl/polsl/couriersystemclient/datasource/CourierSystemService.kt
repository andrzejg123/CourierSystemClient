package pl.polsl.couriersystemclient.datasource

import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONObject
import pl.polsl.couriersystemclient.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

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
}