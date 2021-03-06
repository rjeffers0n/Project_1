package com.game.web;

import com.game.service.accountservices.ModificationService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WithdrawServlet extends HttpServlet {
    ModificationService modificationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        modificationService = (ModificationService) context.getAttribute("modificationService");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
            return;
        }
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        int withdraw = Integer.parseInt(req.getParameter("withdraw"));
        resp.getWriter().write("Your account has been updated!");
        modificationService.withdraw(withdraw,username);
    }
}
