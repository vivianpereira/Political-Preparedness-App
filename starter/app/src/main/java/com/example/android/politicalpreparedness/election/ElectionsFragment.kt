package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.OnElectionClickListener
import com.example.android.politicalpreparedness.network.models.Election
import org.koin.android.ext.android.inject

class ElectionsFragment : Fragment() {

    private val _viewModel: ElectionsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val electionAdapter =
            ElectionListAdapter(OnElectionClickListener { navToVoterInfo(it, false) })
        binding.electionRecyclerView.adapter = electionAdapter

        val savedElectionAdapter =
            ElectionListAdapter(OnElectionClickListener { navToVoterInfo(it, true) })
        binding.savedRecyclerView.adapter = savedElectionAdapter

        _viewModel.upcomingElectionsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                electionAdapter.submitList(it)
            }
        })

        _viewModel.savedElectionsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                savedElectionAdapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun navToVoterInfo(election: Election, followed: Boolean) {
        findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election,
                followed
            )
        )
    }

    override fun onResume() {
        super.onResume()
        _viewModel.refreshLoads()
    }
}
