package lk.spark.ncms.dao;

import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    private String userName;
    private String password;
    private String name;
    private int moh;
    private int hospital;

    public User(String userName, String password, String name, int moh, int hospital) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.moh = moh;
        this.hospital = hospital;
    }

    public User(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getMoh() {
        return moh;
    }

    public int getHospital() {
        return hospital;
    }

    public void getData(){
        try {
            Connection con = DBConnectionPool.getInstance().getConnection();
            PreparedStatement stmt = null;
            ResultSet rs = null;

            stmt = con.prepareStatement("SELECT * FROM user WHERE username=?");
            rs = stmt.executeQuery();
        } catch (Exception exception){

        }
    }
}
