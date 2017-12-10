package com.koenig.commonModel;

import com.koenig.commonModel.finance.Expenses;
import com.koenig.commonModel.finance.StandingOrder;

/**
 * Created by Thomas on 04.12.2017.
 */

public enum ItemType {

    EXPENSES, FAMILY, OPERATION, STANDING_ORDER, USER, CATEGORY;

    public static ItemType fromItem(Item item) {
        if (item instanceof Expenses) {
            return EXPENSES;
        } else if (item instanceof Family) {
            return FAMILY;
        } else if (item instanceof Operation) {
            return OPERATION;
        } else if (item instanceof StandingOrder) {
            return STANDING_ORDER;
        } else if (item instanceof User) {
            return USER;
        } else if (item instanceof Category) {
            return CATEGORY;
        }

        throw new RuntimeException("Unknown item type");
    }
}
