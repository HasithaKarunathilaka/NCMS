package lk.spark.ncms.repository;

import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HospitalBedRepository {

    public int getBedId(String hospitalId, String patientId){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int bedId = 0;

        try {
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM hospital_bed WHERE hospital_id = '"+hospitalId+"' AND patient_id IS NULL LIMIT 1");
            rs = stmt.executeQuery();

            if(rs.next() == true) {
                bedId = rs.getInt("id");
            }

        } catch (SQLException e){

        } finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return bedId;
    }

    public String admitPatient(String hospitalId, String patientId, int bedId){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int row = 0;

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE hospital_bed SET patient_id = '"+patientId+"' WHERE hospital_id = '"+hospitalId+"' AND id ='"+bedId+"'");
//            stmt.setString(1, patientId);
//            stmt.setString(2, hospitalId);
            row = stmt.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return (row > 0 ? "success" : "fail");
    }

    public String clearBed(String patientId){
        int rs = 0;
        Connection con = null;
        PreparedStatement stmt = null;

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE hospital_bed SET patient_id = NULL WHERE patient_id =?");
            stmt.setString(1, patientId);
            rs = stmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();

        }finally {
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return rs == 1? "Discharge Success" : "Discharge Error";
    }

    public String getBedCount(String hospitalId){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int reservedBedCount = 0;

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT COUNT(id) AS bedCount FROM hospital_bed WHERE hospital_id = '"+hospitalId+"' AND patient_id IS NOT NULL ");
            rs = stmt.executeQuery();

            while (rs.next()){
                reservedBedCount = rs.getInt("bedCount");
                System.out.println(reservedBedCount);
            }

        }catch (SQLException e){
            e.printStackTrace();

        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }

        return String.valueOf(reservedBedCount);

    }
}
