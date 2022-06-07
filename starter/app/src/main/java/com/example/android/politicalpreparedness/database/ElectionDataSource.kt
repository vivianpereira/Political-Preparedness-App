package com.example.android.politicalpreparedness.database


import com.example.android.politicalpreparedness.network.models.Election

interface ElectionDataSource {
    suspend fun getElections(): List<Election>
}