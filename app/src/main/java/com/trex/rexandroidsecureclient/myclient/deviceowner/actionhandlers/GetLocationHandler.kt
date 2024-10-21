package com.trex.rexandroidsecureclient.deviceowner.actionhandlers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.trex.rexcommon.data.DeviceActions

class GetLocationHandler(
    private val context: Context,
) : BaseActionHandler() {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun handle(sendToServer: Boolean = true) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val googleMapsUrl = convertToGoogleMapsUrl(latitude, longitude)
                    Log.i("LocationHandler", "Google Maps URL: $googleMapsUrl")
                    sendTo(sendToServer, googleMapsUrl)
                } ?: Log.e("LocationHandler", "Location is null")
            }.addOnFailureListener { e ->
                Log.e("LocationHandler", "Error getting location: ${e.message}")
            }
    }

    private fun sendTo(
        server: Boolean,
        googleMapsUrl: String,
    ) {
        if (server) {
            sendToServer(
                DeviceActions.ACTION_GET_LOCATION,
                mapOf("location" to googleMapsUrl),
            )
        } else {
            sendToServerViaSMS(
                context,
                DeviceActions.ACTION_GET_CONTACTS_VIA_MESSAGE,
                mapOf("location" to googleMapsUrl),
            )
        }
    }

    private fun convertToGoogleMapsUrl(
        latitude: Double,
        longitude: Double,
        label: String = "Location",
    ): String {
        val encodedLabel = java.net.URLEncoder.encode(label, "UTF-8")
        return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude&query_place_id=$encodedLabel"
    }
}
