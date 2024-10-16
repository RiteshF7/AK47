package com.trex.rexcommon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class NewDevice(
    var shopId: String,
    val fcmToken: String,
    val imeiOne: String,
    val manufacturer: String = "",
    val brand: String = "",
    val modelNumber: String = "",
    val androidVersion: String = "",
    val imeiTwo: String = "000",
    val isRegComplete: Boolean = false,
    val deviceCode: String = "",
    val costumerName: String = "",
    val costumerPhone: String = "",
    val emiPerMonth: String = "",
    val dueDate: String = "",
    val durationInMonths: String = "",
) : Parcelable {
    // No-argument constructor required for Firestore
    constructor() : this("", "", "")
}
