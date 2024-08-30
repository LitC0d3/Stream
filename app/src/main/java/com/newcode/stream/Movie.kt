package com.newcode.stream

data class MovieResponse(
    val estreno: List<Movie>,
    val terror: List<Movie>
)

data class Movie(
    val description: String,
    val id: Int,
    val photo: String,
    val title: String,
    val url: String,
    val year: Int
)