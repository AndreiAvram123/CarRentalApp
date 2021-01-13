package com.andrei.utils

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.toUnix():Long{
    val  zoneId = ZoneId.systemDefault();
    return  this.atStartOfDay(zoneId).toEpochSecond()

}