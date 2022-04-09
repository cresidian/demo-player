package com.example.demoplayer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.demoplayer.databinding.ListItemTrackBinding
import com.example.demoplayer.data.models.Track

class TracksAdapter(private val tracks: List<Track>, val callback: TrackCallback) :
    RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TrackViewHolder {
        val binding =
            ListItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        with(holder) {
            with(tracks[position]) {
                binding.ivTrackArt.load(artworkUrl100)
                binding.tvTrackName.text = trackName
                binding.tvAlbum.text = primaryGenreName
                binding.tvArtist.text = artistName
                itemView.setOnClickListener {
                    callback.onTrackSelected(previewUrl)
                }
            }
        }
    }

    inner class TrackViewHolder(val binding: ListItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root)
}

interface TrackCallback {
    fun onTrackSelected(url: String)
}