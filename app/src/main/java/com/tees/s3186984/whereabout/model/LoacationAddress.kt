package com.tees.s3186984.whereabout.model

import com.google.android.libraries.places.api.model.Place

data class LocationAddress(
    val address: String = "No address name",
    val landmarks: List<Place> = emptyList<Place>()
)

