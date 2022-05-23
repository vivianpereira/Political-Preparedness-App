package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel : ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElectionsList = MutableLiveData<List<Election>>(
        mutableListOf(createFakeElection())
    )
    val upcomingElectionsList: LiveData<List<Election>>
        get() = _upcomingElectionsList

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info


    private fun createFakeElection() = Election(
        id = 1,
        division = Division(
            id = "1",
            country = "Australia",
            state = "QLD"
        ),
        electionDay = Date(),
        name = "Election 1"
    )
}