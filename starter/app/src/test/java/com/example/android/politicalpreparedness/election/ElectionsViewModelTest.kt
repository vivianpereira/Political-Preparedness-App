package com.example.android.politicalpreparedness.election

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.politicalpreparedness.election.data.FakeDataSource
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@ExperimentalCoroutinesApi
class ElectionsViewModelTest {

    // subject under test
    private lateinit var electionsViewModel: ElectionsViewModel

    // fake repo to be injected into viewmodel
    private lateinit var fakeDataSource: FakeDataSource

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // main coroutines dispatcher for unit testing
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val fakeElection = Election(
        id = 1,
        division = Division(
            id = "1",
            country = "Australia",
            state = "QLD"
        ),
        electionDay = Date(),
        name = "Election 1"
    )

    @Before
    fun setupRepository() {
        stopKoin()

        fakeDataSource = FakeDataSource(listOf(fakeElection))
        electionsViewModel = ElectionsViewModel(
            fakeDataSource
        )
    }

    @Test
    fun getElections() = runBlocking {

        fakeDataSource.getElections()

        mainCoroutineRule.pauseDispatcher()

        val electionItem = electionsViewModel.upcomingElectionsList.getOrAwaitValue().first()
        assertThat(fakeElection.id, `is`(electionItem.id))
        assertThat(fakeElection.name, `is`(electionItem.name))
        assertThat(fakeElection.electionDay, `is`(electionItem.electionDay))
        assertThat(fakeElection.division, `is`(electionItem.division))
    }
}