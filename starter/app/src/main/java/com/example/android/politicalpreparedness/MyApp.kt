package com.example.android.politicalpreparedness

import android.app.Application
import android.location.Geocoder
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import java.util.Locale
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
                VoterInfoViewModel(
                    get()
                )
            }
            viewModel {
                ElectionsViewModel(
                    get()
                )
            }
            viewModel {
                RepresentativeViewModel(
                    get(),
                    get()
                )
            }
            single { CivicsApi.retrofitService }
            single { Dispatchers.IO }
            single<ElectionDataSource> {
                ElectionRepository(
                    get(),
                    get()
                )
            }
            single { ElectionDatabase.getInstance(this@MyApp).electionDao as ElectionDao }
            single { Geocoder(get(), Locale.getDefault()) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModule)
        }
    }
}
