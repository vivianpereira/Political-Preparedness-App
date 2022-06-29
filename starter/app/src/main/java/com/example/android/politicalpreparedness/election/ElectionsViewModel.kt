package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val electionRepository: ElectionDataSource
) : ViewModel() {

    private val _upcomingElectionsList = MutableLiveData<List<Election>>()
    val upcomingElectionsList: LiveData<List<Election>>
        get() = _upcomingElectionsList

    private val _savedElectionsList = MutableLiveData<List<Election>>()
    val savedElectionsList: LiveData<List<Election>>
        get() = _savedElectionsList

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    init {
        getElections()
    }

    private fun getElections() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                _upcomingElectionsList.value = electionRepository.getElections()
                _savedElectionsList.value = electionRepository.getSavedElection()
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e("network error", it) }
            }
            _showProgress.postValue(false)
        }
    }

    fun refreshLoads() {
        getElections()
    }
}
