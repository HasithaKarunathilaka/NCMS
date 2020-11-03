package lk.spark.ncms.service;

import java.sql.SQLException;

public interface UserService {

    public String[] signinUser(String userName, String password) throws SQLException;

//    public String signupUser(User userInformation);
}
