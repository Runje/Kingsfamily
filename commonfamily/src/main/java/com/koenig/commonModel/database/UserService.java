package com.koenig.commonModel.database;

import com.koenig.commonModel.User;

import java.sql.SQLException;

public interface UserService {
    User getUserFromId(String id) throws SQLException;
}
