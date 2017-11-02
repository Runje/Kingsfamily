package com.koenig.communication.messages.finance;

import com.koenig.commonModel.Component;
import com.koenig.communication.messages.TextMessage;

/**
 * Created by Thomas on 27.10.2017.
 */

public class FinanceTextMessages {
    public static final String GET_ALL_EXPENSES = "get_all_expenses";
    public static final String GET_ALL_EXPENSES_FAIL = "get_all_expenses_fail";

    public static TextMessage getAllExpensesMessage() {
        return new TextMessage(Component.FINANCE, GET_ALL_EXPENSES);
    }

    public static TextMessage getAllExpensesMessageFail() {
        return new TextMessage(Component.FINANCE, GET_ALL_EXPENSES_FAIL);
    }
}
