package com.example.android.politicalpreparedness.election

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val electionRepository: ElectionDataSource) : ViewModel() {

    fun setElection(election: Election) {
        _election.value = election
        getVoterInfo()
    }

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private var voterInfo: VoterInfoResponse? = null

    private val _hideVoterInfo = MutableLiveData<Unit>()
    val hideVoterInfo: LiveData<Unit> = _hideVoterInfo

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private fun getVoterInfo() {
        viewModelScope.launch {
            try {
                val election = election.value
                if (election != null && election.division.state.isNotBlank()) {
                    val address =
                        """${getCountry(election)}/${election.division.state.uppercase()}"""
                    val response = electionRepository.getVoterInfo(election.id, address)
                    voterInfo = response
                } else {
                    _hideVoterInfo.value = Unit
                }
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e("network error", it) }
            }
        }
    }

    private fun getCountry(election: Election): String =
        if (election.division.country.uppercase() == "US") {
            "USA"
        } else {
            election.division.country.uppercase()
        }

    //TODO: Add var and methods to save and remove elections to local database

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    fun onLocationClicked() = View.OnClickListener {
        voterInfo?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl.let {
            _url.value = it
        }
    }

    fun onInformationClicked() = View.OnClickListener {
        voterInfo?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl.let {
            _url.value = it
        }
    }
}
