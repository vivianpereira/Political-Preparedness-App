package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter(private val onRepresentativeClickListener: OnRepresentativeClickListener) :
    ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val representativeItem = getItem(position)
        holder.bind(representativeItem)
        holder.itemView.setOnClickListener {
            onRepresentativeClickListener.onClick(representativeItem)
        }
    }
}

class RepresentativeViewHolder(private val binding: ItemRepresentativeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(representativeItem: Representative) {
        binding.data = representativeItem
        binding.profileImage.setImageResource(R.drawable.ic_profile)

        representativeItem.official.channels?.let { showSocialLinks(it) }
        representativeItem.official.urls?.let { showWWWLinks(it) }

        binding.executePendingBindings()
    }

    companion object {
        const val FACEBOOK = "Facebook"
        const val TWITTER = "Twitter"
        const val FACEBOOK_URL = "https://www.facebook.com/"
        const val TWITTER_URL = "https://www.twitter.com/"
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRepresentativeBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookIcon, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitterIcon, twitterUrl)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == FACEBOOK }
            .map { channel -> "${FACEBOOK_URL}${channel.id}" }
            .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == TWITTER }
            .map { channel -> "${TWITTER_URL}${channel.id}" }
            .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }
}

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return ((oldItem.official == newItem.official) && (oldItem.office == newItem.office))
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}

class OnRepresentativeClickListener(val onRepresentativeClickListener: (representative: Representative) -> Unit) {
    fun onClick(representative: Representative) = onRepresentativeClickListener(representative)
}
