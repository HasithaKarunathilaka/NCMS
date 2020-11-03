package lk.spark.ncms.controller;

import com.google.gson.JsonObject;
import lk.spark.ncms.service.InputValidation;
import lk.spark.ncms.service.InputValidationlmpl;
import lk.spark.ncms.service.UserService;
import lk.spark.ncms.service.UserServicelmpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accessType = req.getParameter("type");
        UserService userService = new UserServicelmpl();

        if (accessType.equals("signup")){

//            User userInformation = new User(req.getParameter("userName"), req.getParameter("password"), req.getParameter("name"), Integer.parseInt(req.getParameter("moh")), Integer.parseInt(req.getParameter("hospital")));
            InputValidation inputValidation = new InputValidationlmpl();
            String result = (String) inputValidation.validationSignUpUser(req.getParameter("userName"), req.getParameter("password"), req.getParameter("name"), req.getParameter("moh"), req.getParameter("hospital"));
//            String result = userService.signupUser(userInformation);
//            this.sendResponse(result, resp);

        } else if (accessType.equals("signin")){
            InputValidation inputValidation = new InputValidationlmpl();
            String result[] = new String[2];
            try {
                String userName = req.getParameter("userName");
                String password = req.getParameter("password");
                result = inputValidation.validationSignInUser(userName, password);
                System.out.println(result);
                resp.setContentType("application/json");
                PrintWriter printWriter = resp.getWriter();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("hospitalId", result[0]);
                jsonObject.addProperty("userType", result[1]);
                jsonObject.addProperty("userId", userName);
                printWriter.print(jsonObject.toString());

//                if (result[0].equals("patient")){
//                    System.out.println("Patient Login Success");
//
//                    User user2 = new User(userName);
//                    user2.getData();
//
//                    PrintWriter printWriter = resp.getWriter();
//                    printWriter.println(user2.toString());
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            String result = userService.signinUser(req.getParameter("userName"), req.getParameter("password"));
//            this.sendResponse(result, resp);
        }
//        super.doPost(req, resp);
    }

//    private void sendResponse(String data, HttpServletResponse resp) throws IOException
//    {
//        resp.setContentType("application/json");
//        PrintWriter writer = resp.getWriter();
//        JsonObject json = new JsonObject();
//        json.addProperty("Response", data);
//        writer.print(json.toString());
//        writer.flush();
//    }
}
