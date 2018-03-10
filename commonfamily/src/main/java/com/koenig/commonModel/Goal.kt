package com.koenig.commonModel

import java.nio.ByteBuffer

/**
 * Created by Thomas on 21.01.2018.
 */
class Goal : Item {
    /** Year -> Goal **/
    var goals: MutableMap<Int, Int>
    var userId: String

    constructor(name: String, goals: MutableMap<Int, Int>, userId: String) : super(name) {
        this.goals = goals
        this.userId = userId
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        goals = buffer.goals
        userId = buffer.string
    }

    fun setGoal(year: Int, goal: Int) {
        goals[year] = goal
    }

    override val byteLength: Int
        get() = super.byteLength + 2 + goals.size * 8 + userId.byteLength

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.putShort(goals.size.toShort())
        for ((year, value) in goals) {
            buffer.putInt(year)
            buffer.putInt(value)
        }

        buffer.putString(userId)
    }
}



