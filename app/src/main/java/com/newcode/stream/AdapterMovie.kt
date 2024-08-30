package com.newcode.stream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.newcode.stream.databinding.ItemMovieBinding

class AdapterMovie(private val movie: List<Movie>, private val listener: OnClickListener)
    :RecyclerView.Adapter<AdapterMovie.ViewHolder>(){
        private lateinit var context: Context
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val binding = ItemMovieBinding.bind(view)
            fun setListener(movie: Movie){
                binding.ivMovie.setOnClickListener {
                    listener.onClick(movie)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movie.get(position)
        with(holder){
            Glide.with(context)
                .load(movie.photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivMovie)
            setListener(movie)
        }
    }

    override fun getItemCount(): Int = movie.size
}