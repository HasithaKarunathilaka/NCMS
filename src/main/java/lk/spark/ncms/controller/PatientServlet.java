package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.repository.PatientRepository;
import lk.spark.ncms.service.InputValidation;
import lk.spark.ncms.service.InputValidationlmpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PatientServlet")
public class PatientServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String district = req.getParameter("district");
        String locationX = req.getParameter("locationX");
        String locationY = req.getParameter("locationY");
        String gender = req.getParameter("gender");
        String contact = req.getParameter("contact");
        String email = req.getParameter("email");
        String age = req.getParameter("age");

//        Patient patientInformation = new Patient(req.getParameter("firstName"),req.getParameter("lastName"), req.getParameter("district"), Integer.parseInt(req.getParameter("locationX")), Integer.parseInt(req.getParameter("locationY")), req.getParameter("gender"), req.getParameter("contact"), req.getParameter("email"), Integer.parseInt(req.getParameter("age")));
        InputValidation inputValidation = new InputValidationlmpl();
        String [] result = inputValidation.validationPatientRegister(firstName ,lastName , district, locationX, locationY, gender, contact, email, age);
//        PatientService patientService = new PatientServicelmpl();
//        String result = patientService.registerPatient(patientInformation);
//        this.sendResponse(result, resp);
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hospitalId", result[0]);
        jsonObject.addProperty("bedId", result[1]);
        jsonObject.addProperty("patientId", result[2]);
        printWriter.print(jsonObject.toString());

    }

//    private void sendResponse(String[] data, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        PrintWriter writer = resp.getWriter();
//        JsonObject json = new JsonObject();
//        json.addProperty("Response", data);
//        writer.print(json.toString());
//        writer.flush();
//    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String reason = req.getParameter("reason");
        String userType = req.getParameter("userType");
        String hospitalId = req.getParameter("userId");

        if(userType.equals("moh") && reason.equals("fullDetails")){
            PatientRepository patientRepository = new PatientRepository();
            JsonArray patientDetails = patientRepository.getPatientsDetailsList();
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(patientDetails.toString());
        }else if((userType.equals("doctor") ||userType.equals("director")) && reason.equals("fullDetails")){
            PatientRepository patientRepository = new PatientRepository();
            JsonArray patientDetails = patientRepository.getReservedPatientsDetailsList(hospitalId);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(patientDetails.toString());
        }
        if((userType.equals("doctor") ||userType.equals("director")) && reason.equals("onlineDetails")){
            System.out.println(hospitalId.toString());
            PatientRepository patientRepository = new PatientRepository();
            JsonArray patientDetails = patientRepository.getOnlinePatientsDetailsList(hospitalId);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(patientDetails.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPut(req, resp);
        String admitState = req.getParameter("admitState");


        if(admitState.equals("admit")){
            String patientId = req.getParameter("patientId");
            String severityLevel = req.getParameter("severityLevel");
            String doctorId = req.getParameter("doctorId");
            System.out.println(admitState +admitState +severityLevel+ doctorId);
            PatientRepository patientRepository = new PatientRepository();
            String result = patientRepository.admitPatient(patientId, severityLevel, doctorId);
            System.out.println(result);
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Response", result);
            printWriter.print(jsonObject.toString());

        }else if(admitState.equals("discharge")){
            String patientId = req.getParameter("patientId");
            String severityLevel = req.getParameter("severityLevel");
            String doctorId = req.getParameter("doctorId");
            System.out.println(admitState +admitState +severityLevel+ doctorId);
            PatientRepository patientRepository = new PatientRepository();
            String result = patientRepository.dischargePatient(patientId, doctorId);
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Response", result);
            printWriter.print(jsonObject.toString());

        }else if(admitState.equals("update")){
            String id = req.getParameter("id");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String district = req.getParameter("district");
            String xCoordinate = req.getParameter("xCoordinate");
            String yCoordinate = req.getParameter("yCoordinate");
            String gender = req.getParameter("gender");
            String contact = req.getParameter("contact");
            String email = req.getParameter("email");
            String age = req.getParameter("age");
            String severityLevel = req.getParameter("severityLevel");
            String admitDate = req.getParameter("admitDate");

            PatientRepository patientRepository = new PatientRepository();
            String result = patientRepository.updatePatient(id, firstName, lastName, district, xCoordinate, yCoordinate, gender, contact, email, age, severityLevel, admitDate);
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Response", result);
            printWriter.print(jsonObject.toString());

        }else {
            String patientId = req.getParameter("patientId");
            String severityLevel = req.getParameter("severityLevel");
            String doctorId = req.getParameter("doctorId");
            System.out.println(admitState +admitState +severityLevel+ doctorId);
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Response", "Connection Error");
            printWriter.print(jsonObject.toString());
        }
    }
}
