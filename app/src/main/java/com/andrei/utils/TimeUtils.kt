package com.andrei.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toUnix():Long{
    val  zoneId = ZoneId.systemDefault();
    return  this.atStartOfDay(zoneId).toEpochSecond()

}

/**
 * Extension function used to convert unix time to LocalDate
 */
fun Long.fromUnixToLocalDate():LocalDate =
    Instant.ofEpochMilli(this * 1000).atZone(ZoneId.systemDefault()).toLocalDate()