package com.koenig.commonModel.database

import com.koenig.commonModel.Category

interface CategoryTable : DatabaseItemTable<Category> {
    companion object {
        const val NAME = "category_table"
        const val SUBS = "subs"
    }

    override val tableName: String
        get() = NAME

    override val itemSpecificCreateStatement: String
        get() = ", $SUBS TEXT"

    override val columnNames: List<String>
        get() = listOf(SUBS)

}