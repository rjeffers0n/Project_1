package com.game.web;

import com.game.service.accountservices.AccountDetailService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Logs in and creates a session in the doPost method
 */
public class LoginServlet extends HttpServlet {
    AccountDetailService accountDetailService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        accountDetailService = (AccountDetailService) context.getAttribute("accountDetailService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
//        String destPage = "index.html";
        if(accountDetailService.checkCredentials(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            resp.sendRedirect("pages/portal.html");
//            req.getRequestDispatcher("pages/portal.html").forward(req, resp);
        } else {
            resp.sendRedirect(("index.html"));
        }
    }
}
