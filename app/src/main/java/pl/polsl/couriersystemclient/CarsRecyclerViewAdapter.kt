package pl.polsl.couriersystemclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cars_recycler_list_item.view.*
import pl.polsl.couriersystemclient.models.Car

class CarsRecyclerViewAdapter(private var items: List<Car>, private val carSelectListener: CarSelectListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CarViewHolder(itemView: View, private val carSelectListener: CarSelectListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val carId: TextView = itemView.car_id
        private val carName: TextView = itemView.car_name
        private val selectButton = itemView.select_button

        fun bind(car: Car) {
            carId.text = itemView.context.getString(R.string.id_string, car.id)
            carName.text = if(car.inUse) itemView.context.getString(R.string.car_name, car.name, ", in use") else itemView.context.getString(R.string.car_name, car.name, "")
            selectButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            carSelectListener.onCarSelect(adapterPosition)
        }
    }

    fun submitList(list: List<Car>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        return CarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cars_recycler_list_item, parent, false),
            carSelectListener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CarViewHolder).bind(items[position])
    }

    interface CarSelectListener {
        fun onCarSelect(position: Int)
    }
}