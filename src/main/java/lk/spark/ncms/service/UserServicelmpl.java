package lk.spark.ncms.service;

import lk.spark.ncms.repository.UserRepository;

import java.sql.SQLException;

public class UserServicelmpl implements UserService {

    @Override
    public String[] signinUser(String username, String password) throws SQLException {

        UserRepository userRepository = new UserRepository();
        String[] result = userRepository.userSignin(username, password);
        return result;
    }

//    @Override
//    public String signupUser(User userData){
//
//        UserRepository userRepository = new UserRepository();
////        String result = userRepository.userSignup(userData);
////        return result;
//    }
}
