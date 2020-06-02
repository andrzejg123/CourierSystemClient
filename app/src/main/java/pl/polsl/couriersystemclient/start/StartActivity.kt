package pl.polsl.couriersystemclient.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import pl.polsl.couriersystemclient.MainActivity
import pl.polsl.couriersystemclient.R
import pl.polsl.couriersystemclient.send.SendPackageActivity

class StartActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        setSupportActionBar(toolbar)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.deliverPackagesButton -> startActivity(Intent(this, MainActivity::class.java))
            R.id.sendPackagesButton -> startActivity(Intent(this, SendPackageActivity::class.java))
        }
    }

}
