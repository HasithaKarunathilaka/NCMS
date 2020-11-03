package lk.spark.ncms.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lk.spark.ncms.repository.DocotorRepository;
import lk.spark.ncms.service.DoctorService;
import lk.spark.ncms.service.DoctorServicelmpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DoctorServlet")
public class DoctorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String reason = req.getParameter("reason");
        System.out.println(reason);

        if(reason.equals("bedInformation")){
            String hospitalId = req.getParameter("hospitalId");
            DoctorService doctorService = new DoctorServicelmpl();
            String[] result = doctorService.getHospitalStatistic(hospitalId);
            System.out.println(result);
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("reservedBed", result[0]);
            jsonObject.addProperty("availableBed", result[1]);
            printWriter.print(jsonObject.toString());

        }else if(reason.equals("full")) {
            String hospitalId = req.getParameter("hospitalId");
            DocotorRepository docotorRepository = new DocotorRepository();
            JsonArray doctorDetails = docotorRepository.getDoctorFullDetails(hospitalId);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(doctorDetails.toString());
        }


//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
//        String name = req.getParameter("name");
//        String hospitalId = req.getParameter("hospitalId");
//        String isDirector = req.getParameter("isDirector");
//        String email = req.getParameter("email");
//        System.out.println(hospitalId);
//        DocotorRepository docotorRepository = new DocotorRepository();
//        String result = docotorRepository.registerDoctor(name, hospitalId, isDirector, email);
//        System.out.println(result);
//
//        resp.setContentType("application/json");
//        PrintWriter printWriter = resp.getWriter();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("Response", result);
//        printWriter.print(jsonObject.toString());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPut(req, resp);

        String name = req.getParameter("name");
        String hospitalId = req.getParameter("hospitalId");
        String isDirector = req.getParameter("isDirector");
        String email = req.getParameter("email");
        System.out.println(hospitalId);
        DocotorRepository docotorRepository = new DocotorRepository();
        String result = docotorRepository.registerDoctor(name, hospitalId, isDirector, email);
        System.out.println(result);

        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Response", result);
        printWriter.print(jsonObject.toString());
    }
}
