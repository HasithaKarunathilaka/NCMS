package lk.spark.ncms.repository;

import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public String[] userSignin(String userName, String password) throws SQLException {

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        String name = null;
        String[] result = new String[2];

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM user WHERE user.username = ? AND user.password = ?");
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if(rs.next() == true){

                if(rs.getInt("moh") == 1){
                     result[1] = "moh";
                }else if (rs.getInt("hospital") == 1){
                    DocotorRepository docotorRepository = new DocotorRepository();
                    result = docotorRepository.getLoginDetails(userName);
                    name = "hospital";
                }else {
                    result[1] = "patient";
                }

            }else{
                result[0] = "User Login Failed";

            }

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return result;
    }

    public String userSignup(String userName, String password, String name, int moh, int hospital){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int resultRegister = 0;

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO user (username, password, name, moh, hospital) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setInt(4, moh);
            stmt.setInt(5, hospital);

            resultRegister = stmt.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return resultRegister == 1 ? "Registered Successfully" : "Registration Failed";
    }

    public String getEmail(String userName){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        String email = null;

        try {
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM user WHERE username = ?");
            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            if(rs.next() == true){
                email = rs.getString("password");

            }else {
                email = "Not Found";
            }


        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return email;
    }
}
