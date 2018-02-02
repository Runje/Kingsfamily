package com.koenig.commonModel


import com.koenig.commonModel.finance.BankAccount
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder
import org.joda.time.DateTime
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Thomas on 28.10.2017.
 */

abstract class Byteable {

    val boolLength: Int
        get() = 1

    abstract val byteLength: Int

    val bytes: ByteArray
        get() {
            val buffer = ByteBuffer.allocate(byteLength)
            writeBytes(buffer)
            return buffer.array()
        }

    abstract fun writeBytes(buffer: ByteBuffer)

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

        fun byteToItem(buffer: ByteBuffer): Item {
            val className = Byteable.byteToEnum(buffer, ItemType::class.java)
            return when (className) {
                ItemType.EXPENSES -> Expenses(buffer)
                ItemType.CATEGORY -> Category(buffer)
                ItemType.FAMILY -> Family(buffer)
                ItemType.OPERATION -> Operation<Item>(buffer)
                ItemType.STANDING_ORDER -> StandingOrder(buffer)
                ItemType.USER -> User(buffer)
                ItemType.BANKACCOUNT -> BankAccount(buffer)
                ItemType.GOAL -> Goal(buffer)
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

        fun <T : Byteable> writeBigList(byteables: List<T>, buffer: ByteBuffer) {
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

        fun <T : Byteable> getBigListLength(list: List<T>): Int {
            var size = 4
            for (byteable in list) {
                size += byteable.byteLength
            }

            return size
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

        val ByteBuffer.string: String
            get() {
                return Byteable.byteToString(this)
            }

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

        fun ByteBuffer.putString(text: String) {
            Byteable.writeString(text, this)
        }

        fun ByteBuffer.putDateTime(date: DateTime) {
            Byteable.writeDateTime(date, this)
        }

        val DateTime.byteLength: Int
            get() = dateLength

        val ByteBuffer.dateTime: DateTime
            get() = Byteable.byteToDateTime(this)

        fun write(map: Map<DateTime, String>, buffer: ByteBuffer) {
            buffer.putShort(map.size.toShort())
            map.forEach { (date, text) ->
                buffer.putDateTime(date)
                buffer.putString(text)
            }
        }

        fun shortLength(map: Map<DateTime, String>): Int {
            var size = 2
            map.forEach { (_, text) -> size += dateLength + text.byteLength }

            return size
        }

        fun getBytes(map: Map<DateTime, String>): ByteArray {
            val buffer = ByteBuffer.allocate(shortLength(map))
            write(map, buffer)
            return buffer.array()
        }

        fun byteToShortMap(bytes: ByteArray): MutableMap<DateTime, String> {
            val buffer = ByteBuffer.wrap(bytes)
            val result = mutableMapOf<DateTime, String>()
            val size = buffer.short
            for (i in 0 until size) {
                result[buffer.dateTime] = buffer.string
            }

            return result
        }

    }
}
