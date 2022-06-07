package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionRepository(
    private val apiService: CivicsApiService = CivicsApi.retrofitService
) : ElectionDataSource {

    override suspend fun getElections(): List<Election> = apiService.getElections().elections

    override suspend fun getVoterInfo(
        electionId: Int,
        division: Division
    ): VoterInfoResponse {
        val address = """${division.country}/${division.state}"""
        return CivicsApi.retrofitService.getVoterInfo(address, electionId)
    }
}
