package lk.spark.ncms.repository;

import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HospitalBedRepository {

    public void admitPatient(String hospitalId, String patientId){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("UPDATE hospital_bed SET hospital_bed.patient_id = ? WHERE hospital_bed.hospital_id = ? AND hospital_bed.patient_id IS NULL LIMIT 1");
            stmt.setString(1, patientId);
            stmt.setString(2, hospitalId);
//            rs = stmt.executeQuery();
        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
    }
}
