package com.koenig;

import com.koenig.commonModel.User;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

/**
 * Created by Thomas on 19.11.2017.
 */

public class FamilyConstants {
    public static final long NO_DATE_LONG = 0;
    public static final DateTime NO_DATE = new DateTime(NO_DATE_LONG);
    public static final DateTime UNLIMITED = new DateTime(3000, 12, 31, 23, 59);
    @NotNull
    public static final User GOAL_ALL_USER = new User("ALL_GOAL", "ALL GOAL", "A", new DateTime());
    public static User ALL_USER = new User("ALL", "ALL", "A", new DateTime());
}
