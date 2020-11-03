package lk.spark.ncms.repository;

import lk.spark.ncms.dao.Hospital;
import lk.spark.ncms.db.DBConnectionPool;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Comparator.comparingDouble;

public class HospitalRepository {
    public String registerHospital(Hospital hospitalInformation){
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;

        ResultSet rs2 = null;
        Connection con2 = null;
        PreparedStatement stmt2 = null;

        int resultHospital = 0;
        int resultBed = 0;

        try{
            UUID uID = UUID.randomUUID();
            String uId = uID.toString();

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO hospital (id, name, district, location_x, location_y, build_date) VALUES (?,?,?,?,?,?)");
            stmt.setString(1, uId);
            stmt.setString(2, hospitalInformation.getName());
            stmt.setString(3, hospitalInformation.getDistrict());
            stmt.setInt(4, hospitalInformation.getxCoordinate());
            stmt.setInt(5, hospitalInformation.getyCoordinate());
            stmt.setDate(6, new Date(new java.util.Date().getTime()));
            resultHospital = stmt.executeUpdate();

            con2 = DBConnectionPool.getInstance().getConnection();
            stmt2 = con2.prepareStatement("INSERT INTO hospital_bed (id, hospital_id) VALUES (1,'" + uId + "'),(2,'" + uId + "'),(3,'" + uId + "'),(4,'" + uId + "'),(5,'" + uId + "'),(6,'" + uId + "'),(7,'" + uId + "'),(8,'" + uId + "'),(9,'" + uId + "'),(10,'" + uId + "')");
//            stmt2.setInt(1, Integer.parseInt(uId));
//            stmt2.setString(1, uId);

            resultBed = stmt2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);

            DBConnectionPool.getInstance().close(rs2);
            DBConnectionPool.getInstance().close(stmt2);
            DBConnectionPool.getInstance().close(con2);

        }
        return (resultHospital > 0 && resultBed > 0 ? "Hospital Registration Success" : "Hospital Registration Failed \n" +
                " Maximum Number of Hospitals Reached Already..!! ");

    }

    public String[] assignedHospital(String nearestHospitalId, String patientId){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        int bedId = 0;
        String hospitalId = "";
        String [] result = new String[2];
        HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();

        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT DISTINCT id FROM hospital WHERE id != '"+nearestHospitalId+"'");
            rs = stmt.executeQuery();

            if(rs.next() == true){
                while(rs.next()){
                    if(bedId == 0){
                        hospitalId = rs.getString("id");
                        bedId =  hospitalBedRepository.getBedId(hospitalId, patientId);
                    }

                }
            }
            result[0] = hospitalId;
            result[1] = String.valueOf(bedId);

        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        return result;
    }

    public String getNearestHospital(int patientXCoordinate, int patientYCoordinate){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        Map<String, Double> distance = new HashMap<String, Double>();
        Double calculatedDistance ;
        String nearestHospital = "";
//        String id = null;
//        String pDistrict  ;
//        int test = 0;

//        ArrayList<HospitalsWithBed> hospitalWithBedDetails = new ArrayList<>();
        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM hospital");
            rs = stmt.executeQuery();
//            test = stmt.getFetchSize();
//            id = rs.getString("id");

            while(rs.next()){
                String hospitalID = rs.getString("id");
                Double hospitalXCoordinate = Double.parseDouble(rs.getString("location_x"));
                Double hospitalYCoordinate = Double.parseDouble(rs.getString("location_y"));
                Double patientXLoc = Double.parseDouble(String.valueOf(patientXCoordinate));
                Double patientYLoc = Double.parseDouble(String.valueOf(patientYCoordinate));
                double xDistance = (double) Math.pow(Math.abs(patientXLoc - hospitalXCoordinate), 2);
                double yDistance = (double) Math.pow(Math.abs(patientYLoc - hospitalYCoordinate), 2);
                calculatedDistance = (double) Math.sqrt(xDistance + yDistance);
                distance.put(hospitalID, calculatedDistance);

//                HospitalsWithBed hospitalsWithBed = new HospitalsWithBed(rs.getString(0), rs.getInt(1), rs.getInt(2));
//                hospitalWithBedDetails.add(hospitalsWithBed);
            }
            nearestHospital = Collections.min(distance.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();



        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
//        return hospitalWithBedDetails;
        return nearestHospital;
    }
}
