package lk.spark.ncms.service;

import java.sql.SQLException;

public interface InputValidation {
    public Object validationHospitalRegister(String name, String district, String xCoordinate, String yCoordinate);
    public String[] validationPatientRegister(String firstName, String lastName, String district, String locationX, String locationY, String gender, String contact, String email, String age);
    public Object validationSignUpUser(String username, String password, String name, String moh, String hospital);
    public String[] validationSignInUser(String username, String password) throws SQLException;
}
