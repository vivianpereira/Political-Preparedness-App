package com.example.android.politicalpreparedness.representative

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.OnRepresentativeClickListener
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject

class DetailFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
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
        binding.viewModel = _viewModel

        val representativeAdapter = RepresentativeListAdapter(OnRepresentativeClickListener {})
        binding.representativeRecyclerView.adapter = representativeAdapter

        _viewModel.representative.observe(viewLifecycleOwner, Observer {
            it?.let {
                representativeAdapter.submitList(it)
            }
        })

        binding.buttonSearch.setOnClickListener {
            val address1 = binding.addressLine1.text.toString()
            val state = binding.state.getItemAtPosition(binding.state.selectedItemPosition).toString()
            val address2 = binding.addressLine2.text.toString()
            val city = binding.city.text.toString()
            val zip = binding.zip.text.toString()

            hideKeyboard()
            _viewModel.searchRepresentativesByAddress(
                address1,
                address2,
                city,
                state,
                binding.state.selectedItemPosition,
                zip
            )
        }

        _viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        binding.buttonLocation.setOnClickListener {
            if (checkLocationPermissions()) {
                getLocation()
            }
        }

        return binding.root
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationPermissions(): Boolean {
        return when {
            isPermissionGranted() -> {
                true
            }
            else -> {
                requestPermissions(
                    arrayOf(ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
                false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            _viewModel.searchByLocation(location)
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}

