package com.blsoftware.resqueue.home

data class HomeCardItem (
    val destinationRoute: String,
    val titleResId: Int,
    val iconResId: Int,
    val description: String? = null
)