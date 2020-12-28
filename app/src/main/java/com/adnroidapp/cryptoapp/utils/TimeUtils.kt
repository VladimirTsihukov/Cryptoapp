package com.adnroidapp.cryptoapp.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToTime(timestamp: Long?) : String {
    if (timestamp == null) return ""
    val stamp = Timestamp(timestamp * 1000) // create object TimeStamp
    val date = Date(stamp.time)                  // create Data
    val pattern = "HH:mm:ss"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}