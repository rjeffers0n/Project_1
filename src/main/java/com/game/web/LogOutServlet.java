package com.game.web;

import com.game.service.accountservices.AccountDetailService;
import com.game.service.accountservices.AccountDetailServiceImp;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *logs out and invalidates session in the doGet method
 */
public class LogOutServlet extends HttpServlet {
    AccountDetailService accountDetailService;
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        accountDetailService = (AccountDetailServiceImp) context.getAttribute("accountDetailService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
            return;
        }
        HttpSession session = req.getSession();
        String temp = (String)session.getAttribute("username");
        session.invalidate();
        ((AccountDetailService)getServletContext().getAttribute("accountDetailService")).logOff(temp);
        resp.sendRedirect("index.html");
    }
}
