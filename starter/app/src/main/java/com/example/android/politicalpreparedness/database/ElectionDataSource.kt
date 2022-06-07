package com.example.android.politicalpreparedness.database


import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface ElectionDataSource {
    suspend fun getElections(): List<Election>
    suspend fun getVoterInfo(electionId: Int, division: Division): VoterInfoResponse
}
