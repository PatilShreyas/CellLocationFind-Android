package dev.shreyaspatil.celllocationfinder.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.shreyaspatil.celllocationfinder.R
import dev.shreyaspatil.celllocationfinder.model.response.CellLocation
import dev.shreyaspatil.celllocationfinder.utils.getCurrentCellInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CellLocationActivity : AppCompatActivity() {

    private lateinit var viewModel: CellLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        initViewModel()
        initLocationLiveData()
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        button_find.setOnClickListener(::onClickFindLocation)
        mapView.settings.javaScriptEnabled = true
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            CellLocationViewModelFactory()
        )[CellLocationViewModel::class.java]
    }

    private fun initLocationLiveData() {
        viewModel.locationLiveData.observe(
            this,
            Observer(::onStateChanged)
        )
    }

    private fun onClickFindLocation(view: View) {
        val popupMenu = PopupMenu(this, view)

        val allCellInfo = getCurrentCellInfo(this)
        allCellInfo.forEachIndexed { index, cellInfo ->
            popupMenu.menu.add(0, index, 0, "${cellInfo.radio}")
        }

        popupMenu.setOnMenuItemClickListener {
            viewModel.fetchLocation(allCellInfo[it.itemId])
            true
        }

        popupMenu.show()
    }

    private fun onStateChanged(state: State) {
        when (state) {
            is State.Loading -> {
                progressBar.show()
                mapView.hide()
            }
            is State.Failed -> {
                progressBar.hide()
                mapView.hide()
                showToast("Error: ${state.message}")
            }
            is State.Success -> {
                progressBar.hide()
                mapView.show()
                showLocationInfo(state.response)
            }
        }
    }

    private fun showLocationInfo(cellLocation: CellLocation) {
        text_location.text = getString(
            R.string.text_location_format,
            cellLocation.latitude,
            cellLocation.longitude
        )
        text_address.text = cellLocation.address
        mapView.loadUrl(
            "https://www.google.com/maps/place/${cellLocation.latitude},${cellLocation.longitude}"
        )
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2000
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun View.show() {
    visibility = View.VISIBLE
}

private fun View.hide() {
    visibility = View.GONE
}
