package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val electionRepository: ElectionDataSource) : ViewModel() {

    private val _representative = MutableLiveData<List<Representative>>()
    val representative: LiveData<List<Representative>>
        get() = _representative

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _searchRepresentatives = MutableLiveData<String?>()
    val searchRepresentatives: LiveData<String?>
        get() = _searchRepresentatives

    fun getRepresentatives(address: String) {
        viewModelScope.launch {
            try {
                val (offices, officials) = electionRepository.getRepresentatives(address)
                _representative.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            } catch (e: Exception) {
                Log.e("Representative", e.localizedMessage)
            }
        }
    }

    fun getAddress(address: Address) {
        _address.value = address
    }

    fun searchRepresentativesByAddress(
        address1: String,
        address2: String,
        city: String,
        state: String,
        zip: String
    ) {
        val address = "$address1 $address2 $city, $state $zip"
        _searchRepresentatives.value = address
        getRepresentatives(address)
    }

}
