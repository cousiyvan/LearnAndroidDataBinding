package com.example.databinding.models

data class Post(
    val userId: Long = 0L,
    val id: Int? = null,
    val title: String? = null,
    val body: String? = null
)
