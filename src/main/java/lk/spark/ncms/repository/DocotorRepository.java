package lk.spark.ncms.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocotorRepository {
    public String registerDoctor(String name, String hospitalId, String isDirector, String email){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int rss = 0;
        Connection conn = null;
        PreparedStatement stmtt = null;
        int doctorCount = 1;
        String id = null;
        String result = null;

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT COUNT(id) AS doctorCount FROM doctor");
            rs = stmt.executeQuery();

            while (rs.next()){
                doctorCount = rs.getInt("doctorCount");
                System.out.println(doctorCount);
            }
            id = "D"+(doctorCount+1);

            conn = DBConnectionPool.getInstance().getConnection();
            stmtt = conn.prepareStatement("INSERT INTO doctor (id, name, hospital_id, is_director) VALUES (?,?,?,?)");
            stmtt.setString(1, id);
            stmtt.setString(2, name);
            stmtt.setString(3, hospitalId);
            stmtt.setInt(4, Integer.parseInt(isDirector));

            rss = stmtt.executeUpdate();
            if(rss > 0){
                UserRepository userRepository = new UserRepository();
                String result1 = userRepository.userSignup(id, email, name,0,1);
                if(result1.equals("Registered Successfully")){
                    result = id;

                }else {
                    result = "Registration Failed";
                }

            }else {
                result = "Registration Failed";
            }


        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
            DBConnectionPool.getInstance().close(stmtt);
            DBConnectionPool.getInstance().close(conn);

        }

        return result;
    }

    public JsonArray getDoctorFullDetails(String hospitalID){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        JsonArray doctorDetails = new JsonArray();

        try{
            con = DBConnectionPool.getInstance().getConnection();

            if (hospitalID.equals("full")){
                stmt = con.prepareStatement("SELECT * FROM doctor ORDER BY id");
                rs = stmt.executeQuery();

                while (rs.next()){
                    JsonObject doctorDetail = new JsonObject();
                    doctorDetail.addProperty("id", rs.getString("id"));
                    doctorDetail.addProperty("name", rs.getString("name"));
                    doctorDetail.addProperty("hospitalId", rs.getString("hospital_id"));
                    doctorDetail.addProperty("isDirector", (rs.getString("is_director")).toString());
                    doctorDetails.add(doctorDetail);
                    System.out.println(doctorDetail);
                }

            }else {
                stmt = con.prepareStatement("SELECT * FROM doctor WHERE hospital_id = ?");
                stmt.setString(1, hospitalID);
                rs = stmt.executeQuery();

                while (rs.next()){
                    String id = rs.getString("id");
                    JsonObject doctorDetail = new JsonObject();
                    doctorDetail.addProperty("id", id);
                    doctorDetail.addProperty("name", rs.getString("name"));
                    doctorDetail.addProperty("hospitalId", rs.getString("hospital_id"));
                    doctorDetail.addProperty("isDirector", (rs.getString("is_director")).toString());

                    UserRepository userRepository = new UserRepository();
                    String email = userRepository.getEmail(id);
                    doctorDetail.addProperty("email", email);

                    doctorDetails.add(doctorDetail);
                    System.out.println(doctorDetail);
                }

            }


            System.out.println("Start");


        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return doctorDetails;
    }

    public String[] getLoginDetails(String id){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        String[] result = new String[2];

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM doctor WHERE id = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            while (rs.next()){
                result[0] = rs.getString("hospital_id");
                result[1] = rs.getString("is_director");
            }
            System.out.println("Start");


        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return result;
    }
}
