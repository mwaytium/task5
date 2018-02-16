package com.nc.controller;

import com.nc.JDBCmodel.JDBC;
import com.nc.JPAmodel.UserFacade;
import com.nc.JPAmodel.UserGroupFacade;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = {"/deleteuser", "/deletegroup"})
public class DeleteServlet extends HttpServlet {

    @EJB
    private UserGroupFacade userGroupFacade;

    @EJB
    private JDBC jdbc;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = null;
        String userPath = request.getServletPath();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            id = "id".equals(param) ? request.getParameter(param) : id;
        }

        if ("/deleteuser".equals(userPath)){
            jdbc.removeUser(id);
        } else if ("/deletegroup".equals(userPath)){
            userGroupFacade.remove(userGroupFacade.getById(Integer.parseInt(id)));
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }
}
