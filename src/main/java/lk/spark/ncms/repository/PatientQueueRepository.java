package lk.spark.ncms.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.db.DBConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientQueueRepository {

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

    public String addToQueue(String patientId){

        ResultSet rs = null;
        Connection con = null;
        Connection con2 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        String result = null;
        int queueLength = 1;
        int queueId = 0;
        int [] queue = new int[100];
        int rows = 0;

//        UUID uID = UUID.randomUUID();
//        String uId = uID.toString();
        try {

            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT * FROM patient_queue ORDER BY id ASC");
            rs = stmt.executeQuery();
            int a = 0;

            if(rs.next() == true){
                int id = rs.getInt("id");
                queue[a]=id;
                queueLength = queueLength + 1;
                a++;

                while (rs.next()) {
                    id = rs.getInt("id");
                    queue[a]=id;
                    queueLength = queueLength + 1;
                    a++;
                }
                for(int i=0; i<queueLength; i++){
                    if(queue[i] ==0){
                        queueId = queue[a-1] + 1;
                        break;
                    }
                }
            }else{
                queueId = 1;
            }

            if(queueId!=0) {
                con2 = DBConnectionPool.getInstance().getConnection();
                stmt2 = con2.prepareStatement("INSERT INTO patient_queue (id, patient_id) VALUES ('"+queueId+"', '"+ patientId +"')");
                rows = stmt2.executeUpdate();
            }

        }catch (SQLException e){

            e.printStackTrace();
        }finally {

            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);

            DBConnectionPool.getInstance().close(stmt2);
            DBConnectionPool.getInstance().close(con2);
        }

        return  (rows > 0 ? String.valueOf(queueId) : "fail");
//        return result;
    }

    public JsonArray getDetails(){

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement stmt = null;
        JsonArray queueDetails = new JsonArray();

        try{
            con = DBConnectionPool.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT  * FROM patient_queue");
            rs = stmt.executeQuery();
            System.out.println("Start");
            while (rs.next()){
                System.out.println("While");
                JsonObject queueDetail = new JsonObject();
                queueDetail.addProperty("id", rs.getString("id"));
                queueDetail.addProperty("patientId", rs.getString("patient_id"));
                queueDetails.add(queueDetail);
                System.out.println(queueDetail);
            }

        }catch (SQLException e){
            printSQLException(e);

        }finally {
            DBConnectionPool.getInstance().close(rs);
            DBConnectionPool.getInstance().close(stmt);
            DBConnectionPool.getInstance().close(con);
        }
        System.out.println("End");
        return queueDetails;
    }
}
