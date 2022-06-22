package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(elections: Election)

    @Query("SELECT * FROM election_table")
    suspend fun getAllElections(): List<Election>

    @Query("SELECT * from election_table where id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun removeElectionById(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun clearAllElections()
}
