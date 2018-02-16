package com.nc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.JDBCmodel.JDBC;
import com.nc.JPAmodel.UserGroupFacade;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/search")
public class SearchServlet extends HttpServlet{

    @EJB
    private JDBC jdbc;

    @EJB
    private UserGroupFacade userGroupFacade;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String groupName = req.getParameter("group_name");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        resp.setContentType("application/json");

        if (groupName.equals("") && firstName.equals("") && lastName.equals("")) {
            mapper.writeValue(resp.getOutputStream(), userGroupFacade.getAll());
        }
        else if (firstName.equals("") && lastName.equals("")) {
            mapper.writeValue(resp.getOutputStream(), userGroupFacade.getByName(groupName));
        }
        else {
            mapper.writeValue(resp.getOutputStream(), jdbc.findByUserAndGroup(groupName, firstName, lastName));
        }
    }
}
