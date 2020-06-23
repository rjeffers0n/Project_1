package com.game.web;

import com.game.models.Account;
import com.game.service.accountservices.AccountDetailService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

/**
 * Sends account attributes as a JSON object in the doGet method
 */
public class ProfileServlet extends HttpServlet {
    AccountDetailService accountDetailService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        accountDetailService = (AccountDetailService) context.getAttribute("accountDetailService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher("profile.html").include(req, resp);
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
            return;
        }
        HttpSession session = req.getSession();
        String username = (String)session.getAttribute("username");

        Account acctInfo = accountDetailService.findByID(username);
        String accountJSON = "";
        Writer out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new GsonBuilder().create();
        accountJSON = gson.toJson(acctInfo);
        out.write(accountJSON);
        out.flush();
        out.close();
    }
}
