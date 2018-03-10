package com.koenig.communication.messages.family

import com.koenig.commonModel.Component
import com.koenig.communication.messages.FamilyMessage
import com.koenig.communication.messages.FamilyMessage.Companion.SEPARATOR
import com.koenig.communication.messages.TextMessage

/**
 * Created by Thomas on 27.10.2017.
 */

object FamilyTextMessages {
    val LOGIN = "Login"
    val LOGIN_FAIL = "Login_failed"
    val CREATE_USER_SUCCESS = "Create_User_success"
    val CREATE_USER_FAIL = "Create_User_fail"
    val USERNAME_ALREADY_IN_USE = "Username_already_in_use"
    val FAMILYNAME_ALREADY_IN_USE = "Familyname_already_in_use"
    val CREATE_FAMILY = "Create_Family"
    val CREATE_FAMILY_SUCCESS = "Create_Family_Success"
    val CREATE_FAMILY_FAIL = "Create_Family_Fail"
    val JOIN_FAMILY = "join_Family"
    val JOIN_FAMILY_SUCCESS = "join_Family_Success"
    val JOIN_FAMILY_FAIL = "join_Family_Fail"
    val GET_FAMILY_MEMBER = "get_Family_Member"
    val GET_FAMILY_MEMBER_FAIL = "get_Family_Member_Fail"

    val familyMemberMessage: FamilyMessage
        get() = TextMessage(Component.FAMILY, GET_FAMILY_MEMBER)

    fun CreateFamilyMessage(familyName: String): FamilyMessage {
        return TextMessage(Component.FAMILY, CREATE_FAMILY + SEPARATOR + familyName)
    }

    fun JoinFamilyMessage(familyName: String): FamilyMessage {
        return TextMessage(Component.FAMILY, JOIN_FAMILY + SEPARATOR + familyName)
    }

    fun loginMessage(): FamilyMessage {
        return TextMessage(Component.FAMILY, LOGIN)
    }
}
