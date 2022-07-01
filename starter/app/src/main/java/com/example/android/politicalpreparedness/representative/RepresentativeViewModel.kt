package com.example.android.politicalpreparedness.representative

import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val electionRepository: ElectionDataSource,
    private val geocoder: Geocoder,
) : ViewModel() {

    private val _representative = MutableLiveData<List<Representative>>()
    val representative: LiveData<List<Representative>>
        get() = _representative

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    private fun getRepresentatives(address: String) {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                val (offices, officials) = electionRepository.getRepresentatives(address)
                _representative.value =
                    offices.flatMap { office -> office.getRepresentatives(officials) }
            } catch (e: Exception) {
                Log.e("Representative", e.localizedMessage)
                _errorMessage.value = R.string.error_message
            }
            _showProgress.postValue(false)
        }
    }

    private fun setAddress(address: Address) {
        _address.value = address
    }

    fun searchRepresentativesByAddress(
        address1: String,
        address2: String,
        city: String,
        state: String,
        statePosition: Int,
        zip: String
    ) {
        if (statePosition == 0 || address1.isEmpty() || zip.isEmpty()) {
            _errorMessage.value = R.string.error_invalid_address
        } else {
            val address = "$address1 $address2 $city, $state $zip"
            getRepresentatives(address)
        }
    }

    fun searchByLocation(location: Location?) {
        try {
            if (location != null) {
                val address = geoCodeLocation(location)
                setAddress(address)
                getRepresentatives(
                    "${address.line1} ${address.line2} ${address.city}, ${address.state} ${address.zip}"
                )
            } else {
                _errorMessage.value = R.string.error_location_required
            }
        } catch (e: Exception) {
            Log.e("Representative", e.localizedMessage)
            _errorMessage.value = R.string.error_message
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare ?: address.premises,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }
}
