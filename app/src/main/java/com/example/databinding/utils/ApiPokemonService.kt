package com.example.databinding.utils

import com.example.databinding.models.Pokemon
import retrofit2.Response
import retrofit2.http.*

interface ApiPokemonService {
    @GET("pokemons")
    suspend fun getPokemons(): Response<List<Pokemon>>

    @GET("pokemons/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<Pokemon>

    @POST("pokemons")
    suspend fun createPokemon(@Body pokemon: Pokemon): Response<Pokemon>

    @DELETE("pokemons/{id}")
    suspend fun deletePokemon(@Path("id") id: Int): Response<Pokemon>
}