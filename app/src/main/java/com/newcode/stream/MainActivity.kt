package com.newcode.stream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.newcode.stream.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var adapterPremier: AdapterMovie
    private lateinit var adapterHorror: AdapterMovie

    private var listPremierMovie = mutableListOf<Movie>()
    private var listHorrorMovie = mutableListOf<Movie>()
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        GlobalScope.launch(Dispatchers.IO) {
            val service: Endpoints = Connection.ResponseEngine().create(Endpoints::class.java)
            val response: Response<MovieResponse> = service.getDataMovies()
            runOnUiThread {
                if (response.isSuccessful) {
                    for (pelicula in response.body()!!.estreno) {
                        listPremierMovie.add(pelicula)
                        adapterPremier = AdapterMovie(listPremierMovie, this@MainActivity)
                        mBinding.rvPremiere.apply {
                            adapter = adapterPremier
                            layoutManager = LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    for (pelicula in response.body()!!.terror) {
                        for (pelicula in response.body()!!.terror) {
                            listHorrorMovie.add(pelicula)
                            adapterHorror = AdapterMovie(listHorrorMovie, this@MainActivity)
                            mBinding.rvHorror.apply {
                                adapter = adapterHorror
                                layoutManager = LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(movie: Movie) {
        super.onClick(movie)
        val intent = Intent(baseContext, PlaymovieActivity::class.java)
        intent.putExtra("url", movie.url)
        intent.putExtra("title", movie.title) // Pasa el título
        startActivity(intent)
        Toast.makeText(baseContext, movie.title, Toast.LENGTH_SHORT).show()
    }

}
