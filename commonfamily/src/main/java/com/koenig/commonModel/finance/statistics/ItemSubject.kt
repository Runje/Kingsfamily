package com.koenig.commonModel.finance.statistics

import com.koenig.commonModel.database.DatabaseItemTable


/**
 * Created by Thomas on 02.01.2018.
 */

interface ItemSubject<T> {
    fun addDeleteListener(listener: DatabaseItemTable.OnDeleteListener<T>)
    fun addDeleteListener(listener: (T?) -> Unit) {
        addDeleteListener(object : DatabaseItemTable.OnDeleteListener<T> {
            override fun onDelete(item: T?) {
                listener(item)
            }
        })
    }

    fun addUpdateListener(listener: DatabaseItemTable.OnUpdateListener<T>)

    fun addUpdateListener(listener: (T?, T) -> Unit) {
        addUpdateListener(object : DatabaseItemTable.OnUpdateListener<T> {
            override fun onUpdate(oldItem: T?, newItem: T) {
                listener(oldItem, newItem)
            }
        })
    }

    fun addAddListener(listener: DatabaseItemTable.OnAddListener<T>)
    fun addAddListener(listener: (T) -> Unit) {
        addAddListener(object : DatabaseItemTable.OnAddListener<T> {
            override fun onAdd(item: T) {
                listener(item)
            }
        })
    }
}
