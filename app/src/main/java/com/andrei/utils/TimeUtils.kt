package com.andrei.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.toUnix():Long{
    val  zoneId = ZoneId.systemDefault();
    return  this.atStartOfDay(zoneId).toEpochSecond()

}

fun LocalDate.toDate():Date{
   return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}

/**
 * Extension function used to convert unix time to LocalDate
 */
fun Long.fromUnixToLocalDate():LocalDate =
    Instant.ofEpochMilli(this * 1000).atZone(ZoneId.systemDefault()).toLocalDate()


fun LocalDate.formatWithPattern(pattern: String = "dd LLLL yyyy"):String{
    return this.format(DateTimeFormatter.ofPattern(pattern))
}