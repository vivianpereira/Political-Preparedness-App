package com.example.android.politicalpreparedness.election

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val electionRepository: ElectionDataSource) : ViewModel() {

    private var followed = false
    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private var voterInfo: VoterInfoResponse? = null

    private val _hideVoterInfo = MutableLiveData<Unit>()
    val hideVoterInfo: LiveData<Unit> = _hideVoterInfo

    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private val _followButton = MutableLiveData(R.string.follow_election_text_button)
    val followButton: LiveData<Int>
        get() = _followButton

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int>
        get() = _errorMessage

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress: LiveData<Boolean>
        get() = _showProgress

    fun initialise(election: Election, followed: Boolean) {
        _election.value = election
        this.followed = followed
        getVoterInfo()
        setUpFollowedButton(followed, election)
    }

    private fun setUpFollowedButton(followed: Boolean, election: Election) {
        viewModelScope.launch {
            if (!followed) {
                val savedElection = electionRepository.getElectionById(election.id)
                this@VoterInfoViewModel.followed = savedElection != null
            }

            if (this@VoterInfoViewModel.followed) {
                _followButton.value = R.string.unfollow_election_text_button
            } else {
                _followButton.value = R.string.follow_election_text_button
            }
        }
    }

    private fun getVoterInfo() {
        _showProgress.value = true
        viewModelScope.launch {
            try {
                val election = election.value
                if (election != null && election.division.state.isNotBlank()) {
                    val address = getAddress(election)
                    voterInfo = electionRepository.getVoterInfo(election.id, address)
                } else {
                    _hideVoterInfo.value = Unit
                }
            } catch (e: Exception) {
                e.localizedMessage?.let { Log.e("network error", it) }
                _errorMessage.value = R.string.error_message
            }
            _showProgress.postValue(false)
        }
    }

    private fun getAddress(election: Election): String =
        """${election.division.country}/${election.division.state}""".uppercase()

    fun onLocationClicked() = View.OnClickListener {
        val urlClicked =
            voterInfo?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
                ?: voterInfo?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
        urlClicked?.let {
            _url.value = it
        } ?: run {
            _errorMessage.value = R.string.error_no_information
        }
    }

    fun onInformationClicked() = View.OnClickListener {
        val urlClicked = voterInfo?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
            ?: voterInfo?.state?.get(0)?.electionAdministrationBody?.electionInfoUrl
        urlClicked?.let {
            _url.value = it
        } ?: run {
            _errorMessage.value = R.string.error_no_information
        }
    }

    fun onFollowClicked() {
        viewModelScope.launch {
            election.value?.let {
                if (followed) {
                    electionRepository.removeElectionById(it.id)
                    _followButton.value = R.string.follow_election_text_button
                    followed = false
                } else {
                    electionRepository.saveElection(it)
                    _followButton.value = R.string.unfollow_election_text_button
                    followed = true
                }
            }
        }
    }
}
