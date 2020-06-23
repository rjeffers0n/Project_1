package com.game.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import com.game.service.accountservices.CreationService;

/**
 * creates account and save to the repo in doPost
 */
public class CreateServlet extends HttpServlet {
    CreationService creationService;

    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        creationService = (CreationService) context.getAttribute("creationService");
    }

    // Below creates new account and adds to account table in database. Will only fill 3 columns.
    // In account updating will allow addition of additional info into remaining columns if needed.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer out = resp.getWriter();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        if(creationService.signUp(username, password, email)){
            resp.sendRedirect("index.html");
        }
        else {
            resp.sendRedirect("pages/signup.html");
        }
    }
}
