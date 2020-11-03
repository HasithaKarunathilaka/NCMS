package lk.spark.ncms.controller;

import com.google.gson.JsonObject;
import lk.spark.ncms.repository.PatientRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "StatisticServlet")
public class StatisticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String type = req.getParameter("type");
        if(type.equals("country")){
            PatientRepository patientRepository = new PatientRepository();
            int totalPatient = patientRepository.getTotalCount();
            int admittedCount = patientRepository.getAdmittedCount();
            int totalDischarge = totalPatient - admittedCount;

            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("totalPatient", totalPatient);
            jsonObject.addProperty("admittedCount", admittedCount);
            jsonObject.addProperty("totalDischarge", totalDischarge);
            printWriter.print(jsonObject.toString());
        }
    }
}
