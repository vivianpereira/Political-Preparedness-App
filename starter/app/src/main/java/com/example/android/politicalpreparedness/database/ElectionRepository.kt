package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ElectionRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val apiService: CivicsApiService = CivicsApi.retrofitService
) : ElectionDataSource {

    override suspend fun getElections(): List<Election> = apiService.getElections().elections
}