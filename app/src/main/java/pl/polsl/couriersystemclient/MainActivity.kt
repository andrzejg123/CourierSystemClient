package pl.polsl.couriersystemclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.couriersystemclient.datasource.DataSource
import pl.polsl.couriersystemclient.map.MapActivity
import pl.polsl.couriersystemclient.models.Car
import pl.polsl.couriersystemclient.models.LatLng

class MainActivity : AppCompatActivity(), CarsRecyclerViewAdapter.CarSelectListener, MainActivityCallback {

    private lateinit var carsRecyclerView : RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val controller = MainActivityController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carsRecyclerView = findViewById<RecyclerView>(R.id.cars_recycler_view)
        viewManager = LinearLayoutManager(this)
        carsRecyclerView.layoutManager = viewManager
        controller.init()

    }

    override fun onCarSelect(position: Int) {
        controller.selectCar(position)
    }

    override fun notifyRecyclerView(data: List<Car>) {
        val viewAdapter = CarsRecyclerViewAdapter(data, this)
        carsRecyclerView.adapter = viewAdapter
    }

    override fun routeToMap() {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("selectedCar", controller.selectedCar)
        startActivity(intent)
    }


}
