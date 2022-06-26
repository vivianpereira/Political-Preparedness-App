package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface ElectionDataSource {
    suspend fun getElections(): List<Election>
    suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse
    suspend fun saveElection(election: Election)
    suspend fun removeElectionById(id: Int)
    suspend fun getElectionById(id: Int): Election?
    suspend fun getSavedElection(): List<Election>
    suspend fun getRepresentatives(address: String): RepresentativeResponse
}
