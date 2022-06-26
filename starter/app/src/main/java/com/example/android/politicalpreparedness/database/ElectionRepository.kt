package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.Representative

class ElectionRepository(
    private val electionDao: ElectionDao,
    private val apiService: CivicsApiService
) : ElectionDataSource {

    override suspend fun getElections(): List<Election> = apiService.getElections().elections

    override suspend fun getVoterInfo(
        electionId: Int,
        address: String
    ): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(address, electionId)
    }

    override suspend fun getSavedElection(): List<Election> = electionDao.getAllElections()

    override suspend fun removeElectionById(id: Int) {
        electionDao.removeElectionById(id)
    }

    override suspend fun saveElection(election: Election) {
        electionDao.insertElection(election)
    }

    override suspend fun getElectionById(id: Int): Election? {
        return electionDao.getElectionById(id)
    }

    override suspend fun getRepresentatives(
        address: String
    ): RepresentativeResponse {
        return CivicsApi.retrofitService.getRepresentatives(address)
    }
}
