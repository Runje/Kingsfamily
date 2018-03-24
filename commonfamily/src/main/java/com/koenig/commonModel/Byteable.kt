package com.koenig.commonModel


import com.koenig.FamilyConstants
import com.koenig.commonModel.finance.BankAccount
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder
import org.joda.time.*
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Thomas on 28.10.2017.
 */

interface Byteable {

    val boolLength: Int
        get() = 1

    val byteLength: Int

    val bytes: ByteArray
        get() {
            val buffer = ByteBuffer.allocate(byteLength)
            writeBytes(buffer)
            return buffer.array()
        }

    fun writeBytes(buffer: ByteBuffer)

    companion object {
        const val encoding = "ISO-8859-1"
        const val stringLengthSize = 2

        fun getStringLength(name: String): Int {
            return name.length + stringLengthSize
        }

        fun stringToByte(string: String): ByteArray {
            val buffer = ByteBuffer.allocate(2 + string.length)
            writeString(string, buffer)
            return buffer.array()
        }

        fun writeString(string: String, buffer: ByteBuffer) {
            try {
                if (string.length > java.lang.Short.MAX_VALUE) {
                    throw RuntimeException("String longer than Short.Max_value")
                }
                buffer.putShort(string.length.toShort())
                buffer.put(string.toByteArray(charset(encoding)))
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e.message)
            }

        }

        fun byteToString(buffer: ByteBuffer): String {
            val length = buffer.short
            val bytes = ByteArray(length.toInt())
            buffer.get(bytes)
            return String(bytes, Charset.forName(encoding)).trim { it <= ' ' }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Item> byteToItem(buffer: ByteBuffer): T {
            val className = Byteable.byteToEnum(buffer, ItemType::class.java)
            return when (className) {
                ItemType.EXPENSES -> Expenses(buffer) as T
                ItemType.CATEGORY -> Category(buffer) as T
                ItemType.FAMILY -> Family(buffer) as T
                ItemType.OPERATION -> Operation<Item>(buffer) as T
                ItemType.STANDING_ORDER -> StandingOrder(buffer) as T
                ItemType.USER -> User(buffer) as T
                ItemType.BANKACCOUNT -> BankAccount(buffer) as T
                ItemType.GOAL -> Goal(buffer) as T
            }

            @Suppress("UNREACHABLE_CODE")
            throw RuntimeException("Unsupported item: " + className.toString())
        }

        fun writeBool(b: Boolean, buffer: ByteBuffer) {
            buffer.put(booleanToByte(b))
        }

        fun booleanToByte(b: Boolean): Byte {
            return (if (b) 1 else 0).toByte()
        }

        fun byteToBoolean(buffer: ByteBuffer): Boolean {
            return byteToBoolean(buffer.get())
        }

        fun byteToBoolean(b: Byte?): Boolean {
            return b == 1.toByte()
        }

        fun byteToDateTime(buffer: ByteBuffer): DateTime {
            return DateTime(buffer.long)
        }

        fun writeDateTime(dateTime: DateTime, buffer: ByteBuffer) {
            buffer.put(dateTimeToBytes(dateTime))
        }

        val dateLength: Int
            get() = 8

        fun dateTimeToBytes(dateTime: DateTime): ByteArray {
            val buffer = ByteBuffer.allocate(dateLength)
            buffer.putLong(dateTime.millis)
            return buffer.array()
        }

        fun getEnumLength(operator: Enum<*>): Int {
            return getStringLength(operator.name)
        }

        fun writeEnum(e: Enum<*>, buffer: ByteBuffer) {
            writeString(e.name, buffer)
        }

        fun <T : Enum<T>> byteToEnum(buffer: ByteBuffer, enumClass: Class<T>): T {
            return java.lang.Enum.valueOf<T>(enumClass, byteToString(buffer))
        }

        fun getItemLength(item: Item): Int {
            return Byteable.getEnumLength(ItemType.fromItem(item)) + item.byteLength
        }

        fun <T : Item> writeItem(item: T, buffer: ByteBuffer) {
            writeEnum(ItemType.fromItem(item), buffer)
            item.writeBytes(buffer)
        }

        fun <T : Byteable> writeList(byteables: List<T>, buffer: ByteBuffer) {
            buffer.putShort(byteables.size.toShort())
            for (byteable in byteables) {
                byteable.writeBytes(buffer)
            }
        }

        fun <T : Byteable> writeBigList(byteables: Collection<T>, buffer: ByteBuffer) {
            buffer.putInt(byteables.size)
            for (byteable in byteables) {
                byteable.writeBytes(buffer)
            }
        }

        fun <T : Byteable> getListLength(byteables: List<T>): Int {
            var size = 2
            for (byteable in byteables) {
                size += byteable.byteLength
            }

            return size
        }

        fun <T : Byteable> getBigListLength(list: Collection<T>): Int {
            return 4 + list.sumBy { it.byteLength }
        }

        fun goalsToByte(goals: Map<Int, Int>): ByteArray {
            val size = 2 + goals.size * 8

            val buffer = ByteBuffer.allocate(size)
            buffer.putShort(goals.size.toShort())
            for ((year, value) in goals) {
                buffer.putInt(year)
                buffer.putInt(value)
            }

            return buffer.array()
        }


        fun bytesToGoals(bytes: ByteArray?): MutableMap<Int, Int> {
            val buffer = ByteBuffer.wrap(bytes)
            return buffer.goals
        }


        fun writeShortMap(map: Map<LocalDate, String>, buffer: ByteBuffer) {
            buffer.putShort(map.size.toShort())
            map.forEach { (date, text) ->
                date.writeBytes(buffer)
                buffer.putString(text)
            }
        }

        fun shortLength(map: Map<LocalDate, String>): Int {
            var size = 2
            map.forEach { (day, text) -> size += day.byteLength + text.byteLength }

            return size
        }


        fun getBytesShortMap(map: Map<LocalDate, String>): ByteArray {
            val buffer = ByteBuffer.allocate(shortLength(map))
            writeShortMap(map, buffer)
            return buffer.array()
        }

        fun byteToShortMap(bytes: ByteArray): MutableMap<LocalDate, String> {
            val buffer = ByteBuffer.wrap(bytes)
            val result = mutableMapOf<LocalDate, String>()
            val size = buffer.short
            for (i in 0 until size) {
                result[buffer.localDate] = buffer.string
            }

            return result
        }

        const val boolLength: Int = 1

    }
}

val ByteBuffer.string: String
    get() {
        return Byteable.byteToString(this)
    }

fun YearMonth.writeBytes(buffer: ByteBuffer) {
    buffer.put(monthOfYear.toByte())
    buffer.putShort(year.toShort())
}

val ByteBuffer.yearMonth: YearMonth
    get() = YearMonth(get().toInt(), short.toInt())

val ByteBuffer.goals: MutableMap<Int, Int>
    get() {
        val size = short
        val goals = HashMap<Int, Int>(size.toInt())
        for (i in 1..size) {
            goals[int] = int
        }

        return goals
    }

val String.byteLength: Int
    get() = Byteable.getStringLength(this)

val DateTime.byteLength: Int
    get() = Byteable.dateLength

val YearMonth.byteLength: Int
    get() = 4

val ByteBuffer.dateTime: DateTime
    get() = Byteable.byteToDateTime(this)

val ByteBuffer.boolean: Boolean
    get () = Byteable.byteToBoolean(this)

fun ByteBuffer.putString(text: String) {
    Byteable.writeString(text, this)
}

fun ByteBuffer.putBoolean(bool: Boolean) {
    Byteable.writeBool(bool, this)
}

fun ByteBuffer.putDateTime(date: DateTime) {
    Byteable.writeDateTime(date, this)
}

val ByteBuffer.localDate
    get() = int.toLocalDate()

fun LocalDate.writeBytes(buffer: ByteBuffer) {
    buffer.putInt(toInt())
}

fun LocalDate.toInt(): Int {
    val days = Days.daysBetween(FamilyConstants.BEGIN_LOCAL_DATE, this)
    return days.days
}

val LocalDate.byteLength
    get() = 4

fun Int.toLocalDate(): LocalDate = FamilyConstants.BEGIN_LOCAL_DATE.plusDays(this)
fun Int.toYearMonth(): YearMonth = FamilyConstants.BEGIN_YEAR_MONTH.plusMonths(this)

fun YearMonth.toInt(): Int {
    return Months.monthsBetween(FamilyConstants.BEGIN_YEAR_MONTH, this).months
}

fun Boolean.toInt(): Int = if (this) 1 else 0