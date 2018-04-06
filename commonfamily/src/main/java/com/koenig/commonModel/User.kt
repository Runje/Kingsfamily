package com.koenig.commonModel

import org.joda.time.DateTime
import java.nio.ByteBuffer
import java.util.*

class User : Item {
    lateinit var family: String
        private set
    lateinit var abbreviation: String
    lateinit var birthday: DateTime
        private set
    lateinit var permissions: MutableMap<Component, Permission>

    override val byteLength: Int
        get() = super.byteLength + Byteable.Companion.getStringLength(abbreviation) + Byteable.Companion.getStringLength(family) + Byteable.Companion.dateLength + getPermissionLength(permissions).toInt()

    constructor(name: String) : super(name)

    constructor(id: String, name: String, abbreviation: String, family: String, birthday: DateTime, permissions: MutableMap<Component, Permission>) : super(id, name) {
        this.abbreviation = abbreviation
        this.family = family
        this.birthday = birthday
        this.permissions = permissions
    }

    constructor(name: String, abbreviation: String, family: String, birthday: DateTime, permissions: MutableMap<Component, Permission>) : super(name) {
        this.abbreviation = abbreviation
        this.family = family
        this.birthday = birthday
        this.permissions = permissions
    }

    constructor(name: String, family: String, birthday: DateTime, permissions: MutableMap<Component, Permission>) : super(name) {
        this.abbreviation = name[0].toString()
        this.family = family
        this.birthday = birthday
        this.permissions = permissions
    }

    constructor(name: String, family: String, birthday: DateTime) : super(name) {
        this.abbreviation = name[0].toString()
        this.family = family
        this.birthday = birthday
        permissions = Permission.CreateNonePermissions()
    }

    constructor(id: String, name: String, family: String, birthday: DateTime) : super(id, name) {
        this.abbreviation = name[0].toString()
        this.family = family
        this.birthday = birthday
        permissions = Permission.CreateNonePermissions()
    }

    constructor(buffer: ByteBuffer) : super(buffer) {
        abbreviation = Byteable.Companion.byteToString(buffer)
        family = Byteable.Companion.byteToString(buffer)
        birthday = Byteable.Companion.byteToDateTime(buffer)
        permissions = bytesToPermissions(buffer)
    }

    fun getPermission(component: Component): Permission {
        return permissions[component]!!
    }

    fun setPermission(component: Component, permission: Permission): Permission {
        return permissions.put(component, permission)!!
    }

    override fun writeBytes(buffer: ByteBuffer) {
        super.writeBytes(buffer)
        buffer.put(Byteable.Companion.stringToByte(abbreviation))
        buffer.put(Byteable.Companion.stringToByte(family))
        buffer.put(Byteable.Companion.dateTimeToBytes(birthday))
        buffer.put(permissionsToBytes(permissions))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val user = other as User?

        return id == user!!.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "User(name='$name', family='$family', abbreviation='$abbreviation', birthday=$birthday)"
    }

    companion object {

        fun permissionsToBytes(permissions: Map<Component, Permission>): ByteArray {
            val buffer = ByteBuffer.allocate(getPermissionLength(permissions).toInt())
            buffer.putShort(permissions.size.toShort())
            for ((component, permission) in permissions) {
                buffer.put(component.toBytes())
                permission.write(buffer)
            }

            return buffer.array()
        }

        fun bytesToPermissions(buffer: ByteBuffer): HashMap<Component, Permission> {
            val size = buffer.short
            val result = HashMap<Component, Permission>(size.toInt())
            for (i in 0 until size) {
                val component = Component.read(buffer)
                val permission = Permission.read(buffer)
                result[component] = permission
            }

            return result
        }

        private fun getPermissionLength(permissions: Map<Component, Permission>): Short {
            var size = 2
            for ((component, permission) in permissions) {
                size += (component.toString().length + 2).toShort()
                size += (permission.toString().length + 2).toShort()
            }
            return size.toShort()
        }
    }
}
