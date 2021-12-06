package com.example.databinding.models

import java.util.*

data class Pokemon(
    val id: Int,
    val hp: Int,
    val cp: Int,
    val name: String,
    val picture: String,
    val types: List<String>,
    val created: Date
)
