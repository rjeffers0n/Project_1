package com.game.web;

import com.game.service.accountservices.AccountDetailService;
import com.game.service.accountservices.ModificationService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * modifies username and passwords in put method
 */
public class AccEditServlet extends HttpServlet {
    ModificationService modificationService;
    AccountDetailService accountDetailService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        accountDetailService = (AccountDetailService) context.getAttribute("accountDetailService");
        modificationService = (ModificationService) context.getAttribute("modificationService");
    }

    /**
     * Changes password
     * @param req request
     * @param resp response
     * @throws ServletException SE
     * @throws IOException IOE
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
        }
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String password = req.getParameter("new_password");
        modificationService.changePassword(password, username);
        resp.sendRedirect("pages/myprofile.html");
    }

    /**
     * Changes email
     * @param req request
     * @param resp response
     * @throws ServletException SE
     * @throws IOException IOE
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
            return;
        }
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String bankAccount = req.getParameter("new_bankinfo");
        modificationService.changeBankAccount(bankAccount, username);
        resp.sendRedirect("pages/myprofile.html");
    }

    /**
     * unused
     * @param req request
     * @param resp response
     * @throws ServletException SE
     * @throws IOException IOE
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.isRequestedSessionIdValid()){
            resp.sendRedirect("index.html");
            return;
        }
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String password = req.getParameter("password");
        String bankAccount = req.getParameter("bankAccount");

        resp.getWriter().write("Your account has been updated!");
        modificationService.changePassword(password, username);
        modificationService.changeBankAccount(bankAccount, username);
    }
}
