package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.Election

class VoterInfoViewModel : ViewModel() {

    fun setElection(election: Election) {
        _election.value = election
    }

    //TODO: Add live data to hold voter info
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    //TODO: Add var and methods to populate voter info

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    //TODO: Add var and methods to save and remove elections to local database
    fun getVoterInfo() {
//        viewModelScope.launch {
//            try {
//                val response = electionRepository.getVoterInfo()
//                _voterInfo.value = response
//            } catch (e: Exception) {
//                e.localizedMessage?.let { Log.e("network error", it) }
//            }
//        }
    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}