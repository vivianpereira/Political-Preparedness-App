package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.OnRepresentativeClickListener
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import java.util.Locale
import org.koin.android.ext.android.inject

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

    //TODO: Declare ViewModel
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
            val address1 = binding.addressLine1.text
            val address2 = binding.addressLine2.text
            val state = binding.state.getItemAtPosition(binding.state.selectedItemPosition)
            val city = binding.city.text
            val zip = binding.zip.text
//            _viewModel.findRepresentatives("$address2 $address1, $city, $state, $zip")
//            hideKeyboard()
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

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
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
