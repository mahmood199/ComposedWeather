package com.example.data.model.response

import com.google.gson.annotations.SerializedName


data class LocationResponseItem(
    @SerializedName("addresstype") val addressType: String,
    @SerializedName("boundingbox") val boundingBox: List<String>,
    @SerializedName("category") val category: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("importance") val importance: Double,
    @SerializedName("lat") val lat: String,
    @SerializedName("licence") val licence: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("name") val name: String,
    @SerializedName("osm_id") val osmId: Long,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("place_rank") val placeRank: Long,
    @SerializedName("type") val type: String
)