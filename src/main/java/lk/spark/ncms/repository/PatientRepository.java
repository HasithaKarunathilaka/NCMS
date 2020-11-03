package lk.spark.ncms.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.dao.Patient;
import lk.spark.ncms.db.DBConnectionPool;

import java.sql.*;
import java.util.UUID;

public class PatientRepository {

    public String addPatient(Patient patientInformation){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rss = null;
        Connection conn = null;
        PreparedStatement stmtt = null;
        String id = null;
        int patientCount = 0;

        int resultPatient = 0;
        UUID uId = UUID.randomUUID();

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmtt = conn.prepareStatement("SELECT COUNT(id) AS patientCount FROM patient");
            rss = stmtt.executeQuery();

            while (rss.next()){
                patientCount = rss.getInt("patientCount");
                System.out.println(patientCount);
            }
            id = "P"+(patientCount+1);

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO patient (id, first_name, last_name, district, location_x, location_y, gender, contact, email, age) VALUES (?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, id);
            stmt.setString(2, patientInformation.getFirstName());
            stmt.setString(3, patientInformation.getLastName());
            stmt.setString(4, patientInformation.getDistrict());
            stmt.setInt(5, patientInformation.getxCoordinate());
            stmt.setInt(6, patientInformation.getyCoordinate());
            stmt.setString(7,patientInformation.getGender());
            stmt.setString(8,patientInformation.getContact());
            stmt.setString(9,patientInformation.getEmail());
            stmt.setInt(10,patientInformation.getAge());

            resultPatient = stmt.executeUpdate();

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rss);
            DBConnectionPool.getInstance().close(stmtt);
            DBConnectionPool.getInstance().close(conn);

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return resultPatient == 1 ? id : "Error" ;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public JsonArray getPatientsDetailsList(){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        JsonArray patientsDetails = new JsonArray();

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT  * FROM patient");
            rs = stmt.executeQuery();
            System.out.println("Start");
            while (rs.next()){
                System.out.println("While");
                JsonObject patientDetail = new JsonObject();
                patientDetail.addProperty("id", rs.getString("id"));
                patientDetail.addProperty("firstName", rs.getString("first_name"));
                patientDetail.addProperty("lastName", rs.getString("last_name"));
                patientDetail.addProperty("gender", rs.getString("gender"));
                patientDetail.addProperty("age", rs.getString("age"));
                patientDetail.addProperty("district", rs.getString("district"));
                patientDetail.addProperty("xCoordinate", rs.getString("location_x"));
                patientDetail.addProperty("yCoordinate", rs.getString("location_y"));
                patientDetail.addProperty("contact", rs.getString("contact"));
                patientDetail.addProperty("email", rs.getString("email"));
                patientDetail.addProperty("severityLevel", rs.getString("severity_level"));
                patientDetail.addProperty("admit_date", rs.getString("admit_date"));
                patientDetail.addProperty("admittedBy", rs.getString("admitted_by"));
                patientDetail.addProperty("discharge_date", rs.getString("discharge_date"));
                patientDetail.addProperty("dischargedBy", rs.getString("discharged_by"));
                patientsDetails.add(patientDetail);
                System.out.println(patientDetail);
            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        System.out.println("End");
        return patientsDetails;
    }

    public JsonArray getReservedPatientsDetailsList(String hospitalId){
        ResultSet rs = null;
        ResultSet rss = null;
        Connection con = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtt = null;
        JsonArray patientsDetails = new JsonArray();

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM hospital_bed  WHERE hospital_id ='"+hospitalId+"'");
            rs = stmt.executeQuery();

            while (rs.next()){
                conn = DBConnectionPool.getInstance().getConnection();
                stmtt = conn.prepareStatement("SELECT * FROM patient WHERE id='"+rs.getString("patient_id")+"' ORDER BY admit_date DESC ");
                rss = stmtt.executeQuery();

                while (rss.next()){
                    System.out.println(hospitalId);
                    JsonObject patientDetail = new JsonObject();
                    patientDetail.addProperty("id", rss.getString("id"));
                    patientDetail.addProperty("firstName", rss.getString("first_name"));
                    patientDetail.addProperty("lastName", rss.getString("last_name"));
                    patientDetail.addProperty("gender", rss.getString("gender"));
                    patientDetail.addProperty("age", rss.getString("age"));
                    patientDetail.addProperty("district", rss.getString("district"));
                    patientDetail.addProperty("xCoordinate", rss.getString("location_x"));
                    patientDetail.addProperty("yCoordinate", rss.getString("location_y"));
                    patientDetail.addProperty("contact", rss.getString("contact"));
                    patientDetail.addProperty("email", rss.getString("email"));
                    patientDetail.addProperty("severityLevel", rss.getString("severity_level"));
                    patientDetail.addProperty("admit_date", rss.getString("admit_date"));
                    patientDetail.addProperty("discharge_date", rss.getString("discharge_date"));
                    patientDetail.addProperty("bedId", rs.getString("id"));
                    patientsDetails.add(patientDetail);
                    System.out.println(patientDetail.toString());
                }
                DBConnectionPool.getInstance().close(rss);
                DBConnectionPool.getInstance().close(stmtt);
                DBConnectionPool.getInstance().close(conn);

//                patientDetail.addProperty("admittedBy", rs.getString("admitted_by"));

//                patientDetail.addProperty("dischargedBy", rs.getString("discharged_by"));


            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return patientsDetails;
    }

    public JsonArray getOnlinePatientsDetailsList(String userId){
//        String hospitalId = null;
        String hospitalId = userId;
        ResultSet rs = null;
        ResultSet rss = null;
        Connection con = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtt = null;
        JsonArray patientsDetails = new JsonArray();

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT patient_id FROM hospital_bed  WHERE hospital_id ='"+hospitalId+"'");
            rs = stmt.executeQuery();
            System.out.println("Start2");
            System.out.println(hospitalId);
            while (rs.next()){

                System.out.println(hospitalId);
                conn = DBConnectionPool.getInstance().getConnection();
                stmtt = conn.prepareStatement("SELECT * FROM patient WHERE id='"+rs.getString("patient_id")+"' AND (admit_date IS NULL OR discharge_date IS NULL ) ORDER BY admit_date DESC ");
                rss = stmtt.executeQuery();

                while (rss.next()){
                    JsonObject patientDetail = new JsonObject();
                    patientDetail.addProperty("id", rss.getString("id"));
                    patientDetail.addProperty("severityLevel", rss.getString("severity_level"));
                    patientDetail.addProperty("admit_date", rss.getString("admit_date"));
                    patientDetail.addProperty("discharge_date", rss.getString("discharge_date"));
                    patientsDetails.add(patientDetail);
                    System.out.println(patientDetail.toString());
                }
                DBConnectionPool.getInstance().close(rss);
                DBConnectionPool.getInstance().close(stmtt);
                DBConnectionPool.getInstance().close(conn);
//                patientDetail.addProperty("firstName", rs.getString("first_name"));
//                patientDetail.addProperty("lastName", rs.getString("last_name"));
//                patientDetail.addProperty("gender", rs.getString("gender"));
//                patientDetail.addProperty("age", rs.getString("age"));
//                patientDetail.addProperty("district", rs.getString("district"));
//                patientDetail.addProperty("xCoordinate", rs.getString("location_x"));
//                patientDetail.addProperty("yCoordinate", rs.getString("location_y"));
//                patientDetail.addProperty("contact", rs.getString("contact"));
//                patientDetail.addProperty("email", rs.getString("email"));

//                patientDetail.addProperty("admittedBy", rs.getString("admitted_by"));

//                patientDetail.addProperty("dischargedBy", rs.getString("discharged_by"));


            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        System.out.println("End2");
        return patientsDetails;
    }

    public String admitPatient(String patientId, String severityLevel, String doctorId){
        int rs = 0;
        Connection con = null;
        PreparedStatement stmt = null;

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE patient SET severity_level=?, admit_date=?, admitted_by=? WHERE id=?");
            stmt.setString(1,severityLevel);
            stmt.setDate(2, new Date(new java.util.Date().getTime()));
            stmt.setString(3,doctorId);
            stmt.setString(4,patientId);
            rs =stmt.executeUpdate();
            System.out.println(rs);

        }catch (SQLException e){
            printSQLException(e);

        }finally {
//            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return rs == 1 ? "Admit Success" : "Admit Error";
    }

    public String dischargePatient(String patientId, String doctorId){
        int rs = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        String result = "";

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE patient SET discharge_date=?, discharged_by=? WHERE id=?");
            stmt.setDate(1, new Date(new java.util.Date().getTime()));
            stmt.setString(2,doctorId);
            stmt.setString(3,patientId);
            rs =stmt.executeUpdate();

        }catch (SQLException e){
            printSQLException(e);

        }finally {
//            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        if(rs == 1){
            HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();
            result = hospitalBedRepository.clearBed(patientId);
            return result;
        }else {
            return "Discharge Error";
        }
    }

    public String updatePatient(String id, String firstName, String lastName, String district, String xCoordinate, String yCoordinate, String gender, String contact, String email, String age, String severityLevel, String admitDate){
        int rs = 0;
        Connection con = null;
        PreparedStatement stmt = null;

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE patient SET  id=?,first_name=?,last_name=?,contact=?, district=?,email=?,age=?,location_x=?,location_y=?,admit_date=?,gender=?,severity_level=? WHERE id=?");
            stmt.setString(1,id);
            stmt.setString(2,firstName);
            stmt.setString(3,lastName);
            stmt.setString(4,contact);
            stmt.setString(5, district);
            stmt.setString(6,email);
            stmt.setString(7,age);
            stmt.setString(8, xCoordinate);
            stmt.setString(9, yCoordinate);
            stmt.setString(10,admitDate);
            stmt.setString(11,gender);
            stmt.setString(12,severityLevel);
            stmt.setString(13,id);
            System.out.println(stmt);
            rs = stmt.executeUpdate();

        }catch (SQLException e){
            printSQLException(e);

        }finally {
//            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return rs == 1 ? "Update Success" : "Update Error";
    }

    public int getTotalCount(){
        ResultSet rss = null;
        Connection conn = null;
        PreparedStatement stmtt = null;
        int patientCount = 0;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmtt = conn.prepareStatement("SELECT COUNT(id) AS patientCount FROM patient");
            rss = stmtt.executeQuery();

            while (rss.next()){
                patientCount = rss.getInt("patientCount");
                System.out.println(patientCount);
            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rss);
            DBConnectionPool.getInstance().close(stmtt);
            DBConnectionPool.getInstance().close(conn);

        }
        return patientCount ;

    }

    public int getAdmittedCount(){
        ResultSet rss = null;
        Connection conn = null;
        PreparedStatement stmtt = null;
        int patientCount = 0;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmtt = conn.prepareStatement("SELECT COUNT(id) AS patientCount FROM patient WHERE admit_date IS NOT NULL ");
            rss = stmtt.executeQuery();

            while (rss.next()){
                patientCount = rss.getInt("patientCount");
                System.out.println(patientCount);
            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rss);
            DBConnectionPool.getInstance().close(stmtt);
            DBConnectionPool.getInstance().close(conn);

        }
        return patientCount ;
    }
}
