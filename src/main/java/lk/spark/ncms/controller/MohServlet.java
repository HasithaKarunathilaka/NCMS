package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import lk.spark.ncms.repository.PatientQueueRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MohServlet")
public class MohServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String type = req.getParameter("type");

        if(type.equals("queue")){
            PatientQueueRepository patientQueueRepository = new PatientQueueRepository();
            JsonArray queueDetails = patientQueueRepository.getDetails();
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(queueDetails.toString());
        }
    }
}
