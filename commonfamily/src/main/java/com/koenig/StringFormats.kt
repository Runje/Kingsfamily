package com.koenig

import com.koenig.commonModel.User
import org.joda.time.LocalDate
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Created by Thomas on 23.10.2017.
 */

object StringFormats {

    val dayFormat = "dd.MM.yy"
    private val symbols = buildSymbols()
    private val centFormat = DecimalFormat("0.00", symbols)
    private val euroFormat = DecimalFormat("0", symbols)
    private val percentFormat = DecimalFormat("0.#", symbols)

    private fun buildSymbols(): DecimalFormatSymbols {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ','
        return symbols
    }

    fun localDateTimeToDayString(day: LocalDate): String {
        return day.toString(dayFormat)
    }

    fun centsToCentString(cents: Int): String {
        return StringFormats.centFormat.format(cents / 100.0)
    }

    fun centsToEuroString(cents: Int): String {
        return StringFormats.euroFormat.format(cents / 100.0)
    }

    fun floatToPercentString(percent: Float): String {
        return StringFormats.percentFormat.format(percent * 100.0)
    }


    fun usersToAbbreviationString(users: List<User>): String {
        if (users.size == 0) return ""
        val builder = StringBuilder()
        for (user in users) {
            builder.append(user.abbreviation + ",")
        }

        return builder.substring(0, builder.length - 1)
    }
}
