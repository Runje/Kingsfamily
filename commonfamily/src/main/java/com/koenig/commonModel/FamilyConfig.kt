package com.koenig.commonModel

import com.koenig.FamilyConstants.NO_ID
import com.koenig.FamilyConstants.NO_USER
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime
import org.joda.time.YearMonth
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

/**
 * Created by Thomas on 18.09.2017.
 */
interface FamilyConfig {
    var userId: String
    var familyMembers: List<User>
    // TODO: sync startMonth with server(in Family)
    var startMonth: YearMonth

    val userIdObservable: Observable<String>
    val familyMembersObservable: Observable<List<User>>
    val startDateObservable: Observable<YearMonth>

    fun loadBuffer(key: String): ByteBuffer?
    fun saveBuffer(buffer: ByteBuffer, key: String)
    fun getLastSyncDate(key: String): DateTime
    fun saveLastSyncDate(date: DateTime, key: String)
    val user: User
        get() = familyMembers.find { it.id == userId } ?: NO_USER
}

abstract class FamilyConfigAbstract : FamilyConfig {
    protected val logger = LoggerFactory.getLogger("FamilyConfig")
    override var userId: String = NO_ID
        set(value) {
            field = value
            saveUserId(value)
            userIdObservable.onNext(value)
        }

    override var familyMembers: List<User> = emptyList()
        set(value) {
            field = value
            saveFamilyMembers(value)
            familyMembersObservable.onNext(value)
        }

    override var startMonth = YearMonth()
        set(value) {
            field = value
            saveStartDate(value)
            startDateObservable.onNext(value)
        }

    override val userIdObservable = BehaviorSubject.create<String>()!!
    override val familyMembersObservable = BehaviorSubject.create<List<User>>()!!
    override val startDateObservable = BehaviorSubject.create<YearMonth>()!!

    protected abstract fun loadUserId(): String
    protected abstract fun loadFamilyMembers(): List<User>
    protected abstract fun loadStartDate(): YearMonth

    protected abstract fun saveUserId(userId: String)
    protected abstract fun saveStartDate(date: YearMonth)
    protected abstract fun saveFamilyMembers(members: List<User>)

    fun init() {
        userId = loadUserId()
        familyMembers = loadFamilyMembers()
        startMonth = loadStartDate()
    }
}

