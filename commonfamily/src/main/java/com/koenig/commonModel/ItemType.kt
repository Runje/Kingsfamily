package com.koenig.commonModel

import com.koenig.commonModel.finance.BankAccount
import com.koenig.commonModel.finance.Expenses
import com.koenig.commonModel.finance.StandingOrder

/**
 * Created by Thomas on 04.12.2017.
 */

enum class ItemType {

    EXPENSES, FAMILY, OPERATION, STANDING_ORDER, USER, CATEGORY, BANKACCOUNT, GOAL;

    companion object {

        fun fromItem(item: Item): ItemType {
            return when (item) {
                is Expenses -> EXPENSES
                is Family -> FAMILY
                is Operation<*> -> OPERATION
                is StandingOrder -> STANDING_ORDER
                is User -> USER
                is Category -> CATEGORY
                is BankAccount -> BANKACCOUNT
                is Goal -> GOAL
                else -> throw RuntimeException("Unknown item type")
            }

        }
    }
}
