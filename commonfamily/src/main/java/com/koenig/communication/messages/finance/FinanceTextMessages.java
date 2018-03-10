package com.koenig.communication.messages.finance;

import com.koenig.commonModel.Component;
import com.koenig.communication.messages.FamilyMessage;
import com.koenig.communication.messages.TextMessage;

/**
 * Created by Thomas on 27.10.2017.
 */

public class FinanceTextMessages {
    public static final String GET_ALL_EXPENSES_FAIL = "get_all_expenses_fail";
    public static final String GET_ALL_CATEGORYS_FAIL = "get_all_categorys_fail";
    public static final String AUD_FAIL = "aud_fail";
    public static final String AUD_SUCCESS = "aud_success";



    public static TextMessage getAllExpensesMessageFail() {
        return new TextMessage(Component.FINANCE, GET_ALL_EXPENSES_FAIL);
    }

    public static TextMessage getAllCategorysMessageFail() {
        return new TextMessage(Component.FINANCE, GET_ALL_CATEGORYS_FAIL);
    }

    public static TextMessage getUpdateFailMessage() {
        return new TextMessage(Component.FINANCE, TextMessage.UPDATE_FAIL);
    }

    public static TextMessage audFailMessage(String id) {
        return new TextMessage(Component.FINANCE, AUD_FAIL + FamilyMessage.Companion.getSEPARATOR() + id);
    }

    public static TextMessage audSuccessMessage(String id) {
        return new TextMessage(Component.FINANCE, AUD_SUCCESS + FamilyMessage.Companion.getSEPARATOR() + id);
    }
}
