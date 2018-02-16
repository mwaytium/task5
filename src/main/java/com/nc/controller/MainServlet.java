package com.nc.controller;

import com.nc.JAXBmodel.UserListGroupWrapper;
import com.nc.JDBCmodel.JDBC;
import com.nc.JPAmodel.UserGroupFacade;
import com.nc.JAXBmodel.JAXB;
import com.nc.entity.UserGroupEntity;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(loadOnStartup = 1, urlPatterns = "/index")
public class MainServlet extends HttpServlet {

    @EJB
    private JDBC jdbc;

    @EJB
    private JAXB jaxb;

    @EJB
    private UserGroupFacade userGroupFacade;

    @Override
    public void init() throws ServletException {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("groups", userGroupFacade.getAll());

        List<UserGroupEntity> groups;
        try {
            groups = jaxb.unmarshall().getGroups();
        } catch (Exception e) {
            UserListGroupWrapper wrapper = new UserListGroupWrapper();
            jaxb.marshall(wrapper.getGroups());
            groups = jaxb.unmarshall().getGroups();
        }

        request.setAttribute("backup", groups);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
