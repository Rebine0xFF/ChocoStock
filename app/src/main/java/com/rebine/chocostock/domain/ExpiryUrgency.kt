package com.rebine.chocostock.domain

import com.rebine.chocostock.presentation.common.DateFormatUtils
import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class ExpiryUrgency { URGENT, SOON, FAR, UNKNOWN }

object ExpiryUrgencyCalculator {
    fun compute(expiryDateIso: String?, today: LocalDate = LocalDate.now()): ExpiryUrgency {
        val date = DateFormatUtils.parseIsoOrNull(expiryDateIso) ?: return ExpiryUrgency.UNKNOWN
        val daysLeft = ChronoUnit.DAYS.between(today, date)
        return when {
            daysLeft <= 7 -> ExpiryUrgency.URGENT
            daysLeft <= 30 -> ExpiryUrgency.SOON
            else -> ExpiryUrgency.FAR
        }
    }
}