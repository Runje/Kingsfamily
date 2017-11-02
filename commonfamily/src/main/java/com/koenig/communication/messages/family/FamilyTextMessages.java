package com.koenig.communication.messages.family;

import com.koenig.commonModel.Component;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;

import static com.koenig.communication.messages.FamilyMessage.SEPARATOR;

/**
 * Created by Thomas on 27.10.2017.
 */

public class FamilyTextMessages {
    public static final String LOGIN = "Login";
    public static final String LOGIN_FAIL = "Login_failed";
    public static final String CREATE_USER_SUCCESS = "Create_User_success";
    public static final String CREATE_USER_FAIL = "Create_User_fail";
    public static final String USERNAME_ALREADY_IN_USE = "Username_already_in_use";
    public static final String FAMILYNAME_ALREADY_IN_USE = "Familyname_already_in_use";
    public static final String CREATE_FAMILY = "Create_Family";
    public static final String CREATE_FAMILY_SUCCESS = "Create_Family_Success";
    public static final String CREATE_FAMILY_FAIL = "Create_Family_Fail";
    public static final String JOIN_FAMILY = "join_Family";
    public static final String JOIN_FAMILY_SUCCESS = "join_Family_Success";
    public static final String JOIN_FAMILY_FAIL = "join_Family_Fail";
    public static final String GET_FAMILY_MEMBER = "get_Family_Member";
    public static final String GET_FAMILY_MEMBER_FAIL = "get_Family_Member_Fail";

    public static FamilyMessage CreateFamilyMessage(String familyName) {
        return new TextMessage(Component.FAMILY, CREATE_FAMILY + SEPARATOR + familyName);
    }

    public static FamilyMessage JoinFamilyMessage(String familyName) {
        return new TextMessage(Component.FAMILY, JOIN_FAMILY + SEPARATOR + familyName);
    }

    public static FamilyMessage loginMessage() {
        return new TextMessage(Component.FAMILY, LOGIN);
    }

    public static FamilyMessage getFamilyMemberMessage() {
        return new TextMessage(Component.FAMILY, GET_FAMILY_MEMBER);
    }
}
