package lk.spark.ncms.service;

import lk.spark.ncms.dao.Hospital;
import lk.spark.ncms.dao.Patient;
import lk.spark.ncms.dao.User;

import java.sql.SQLException;

public class InputValidationlmpl implements InputValidation{

//    private String name;
//    private String district;
//    private String xCoordinate ;
//    private int yCoordinate;

    @Override
    public String validationHospitalRegister(String name, String district, String xCoordinate, String yCoordinate){

//        String name = hospitalInformation.getName();
//        String district = hospitalInformation.getDistrict();
//        String xCoordinate = String.valueOf(hospitalInformation.getxCoordinate());
//        String yCoordinate = String.valueOf(hospitalInformation.getyCoordinate());

        if(name.equals(null) || name.isEmpty() || district.equals(null) || district.isEmpty() || xCoordinate.isEmpty() || xCoordinate.equals(null) || yCoordinate.isEmpty() || yCoordinate.equals(null)){
            return "Please Fill the All Fields";

        }else {
            try {
                Hospital hospital = new Hospital(name,district,Integer.parseInt(xCoordinate),Integer.parseInt(yCoordinate));
                HospitalService hospitalService = new HospitalServicelmpl();
                String result = (String) hospitalService.registerHospital(hospital);
                return result;
            }catch (NumberFormatException e){
                return "Please Input in Valid Format";
            }
        }

    }

    @Override
    public String[] validationPatientRegister(String firstName, String lastName, String district, String xCoordinate, String yCoordinate, String gender, String contact, String email, String age) {

        String [] result = new String[2];
        if(firstName.isEmpty() || lastName.isEmpty() || district.isEmpty() || xCoordinate.isEmpty() || yCoordinate.isEmpty() || gender.isEmpty() || contact.isEmpty() || email.isEmpty() || age.isEmpty()){
            result[0] = "Please Fill the All Fields";
            return result;

        }else {

            try {
//                Patient patient = new Patient(firstName,lastName,district,Integer.parseInt(xCoordinate),Integer.parseInt(yCoordinate),gender,contact,email,Integer.parseInt(age));
                Patient patient = new Patient();
                patient.setFirstName(firstName);
                patient.setLastName(lastName);
                patient.setDistrict(district);
                patient.setxCoordinate(Integer.parseInt(xCoordinate));
                patient.setyCoordinate(Integer.parseInt(yCoordinate));
                patient.setGender(gender);
                patient.setContact(contact);
                patient.setEmail(email);
                patient.setAge(Integer.parseInt(age));

                PatientService patientService = new PatientServicelmpl();
                result = patientService.registerPatient(patient);
                return result;

            }catch (NumberFormatException e){
                result[0] = "Please Input in Valid Format";
                return result;
            }
        }
    }

    @Override
    public String validationSignUpUser(String username, String password, String name, String moh, String hospital) {
        if(username.isEmpty() || password.isEmpty() || name.isEmpty() || moh.isEmpty() || hospital.isEmpty()){
            return "Please Fill the All Fields";

        }else {

            try {
                User userInformation = new User(username,password,name,Integer.parseInt(moh),Integer.parseInt(hospital));
                UserService userService = new UserServicelmpl();
//                String result = (String) userService.signupUser(userInformation);
//                return result;

            }catch (NumberFormatException e){
                return "Please Input in Valid Format";
            }
        }
        return null;
    }

    @Override
    public String[] validationSignInUser(String username, String password) throws SQLException {
        String[] result = new String[2];
        if(username.isEmpty() || password.isEmpty()){
            result[0] = "Please Fill the All Fields";
            return result;
        }else{
            UserService userService = new UserServicelmpl();
            result = userService.signinUser(username, password);
            return result;
        }
//        return null;
    }
}
