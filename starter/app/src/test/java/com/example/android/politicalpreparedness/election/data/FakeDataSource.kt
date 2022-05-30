package com.example.android.politicalpreparedness.election.data

import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import java.io.IOException

class FakeDataSource(private val elections: List<Election> = mutableListOf()) :
    ElectionDataSource {

    var shouldReturnError = false

    override suspend fun getElections(): List<Election> {
        if (!shouldReturnError) {
            return elections
        } else {
            throw IOException("Some error!")
        }
    }
}