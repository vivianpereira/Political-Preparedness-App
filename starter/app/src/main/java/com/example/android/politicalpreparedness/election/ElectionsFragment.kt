package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.OnElectionClickListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private val _viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this)[ElectionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val electionAdapter = ElectionListAdapter(OnElectionClickListener { navToVoterInfo(it) })
        binding.electionRecyclerView.adapter = electionAdapter

        _viewModel.upcomingElectionsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                electionAdapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun navToVoterInfo(election: Election) {
        findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election
            )
        )
    }

    //TODO: Refresh adapters when fragment loads

}