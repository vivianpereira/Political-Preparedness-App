package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.OnRepresentativeClickListener
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import org.koin.android.ext.android.inject

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val _viewModel: RepresentativeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val representativeAdapter = RepresentativeListAdapter(OnRepresentativeClickListener {})
        binding.representativeRecyclerView.adapter = representativeAdapter

        _viewModel.representative.observe(viewLifecycleOwner, Observer {
            it?.let {
                representativeAdapter.submitList(it)
            }
        })

        binding.buttonSearch.setOnClickListener {
//            val address1 = binding.addressLine1.text.toString()
//            val state = binding.state.getItemAtPosition(binding.state.selectedItemPosition).toString()
//            val address2 = binding.addressLine2.text.toString()
//            val city = binding.city.text.toString()
//            val zip = binding.zip.text.toString()
            val address1 = "2876 Shore Dr"
            val address2 = ""
            val state = "VA"
            val city = "Virginia Beach"
            val zip = "23451"

            hideKeyboard()
            _viewModel.searchRepresentativesByAddress(address1, address2, state, city, zip)
        }

        binding.buttonLocation.setOnClickListener {
            getLocation()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            try {
                val address = geoCodeLocation(location)
                _viewModel.getAddress(address)
            } catch (e: Exception) {
                Log.e("Representative", e.localizedMessage)
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}

