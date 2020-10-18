package lk.spark.ncms.repository;

import lk.spark.ncms.dao.User;
import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public String userSignin(String userName, String password){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        String name = null;

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT name FROM user WHERE user.username = ? AND user.password = ?");
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if(rs.next() == true){

                if(rs.getInt("moh") == 1){
                    name = "Welcome MOH " + (rs.getString("name"));
                }else if (rs.getInt("hospital") == 1){
                    name = "Welcome Hospital " + (rs.getString("name"));
                }else {
                    name = "Welcome " + (rs.getString("name"));
                }

            }else{
                name = "User Login Failed";

            }

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return name;
    }

    public String userSignup(User userInformation){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int resultRegister = 0;

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO user (username, password, name, moh, hospital) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, userInformation.getUserName());
            stmt.setString(2, userInformation.getPassword());
            stmt.setString(3, userInformation.getName());
            stmt.setInt(4, userInformation.getMoh());
            stmt.setInt(5, userInformation.getHospital());

            resultRegister = stmt.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return resultRegister == 1 ? userInformation.getName() + " Registered Successfully" : "User Registration Failed";
    }
}
