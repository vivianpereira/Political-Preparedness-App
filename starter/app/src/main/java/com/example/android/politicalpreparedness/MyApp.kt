package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            viewModel {
                VoterInfoViewModel()
            }
            viewModel {
                ElectionsViewModel(
                    get()
                )
            }
            single { CivicsApi.retrofitService }
            single { Dispatchers.IO }
            single<ElectionDataSource> {
                ElectionRepository(
                    get()
                )
            }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModule)
        }
    }
}
