package com.nc.controller;

import com.nc.entity.UserEntity;
import com.nc.entity.UserGroupEntity;
import com.nc.JPAmodel.UserFacade;
import com.nc.JPAmodel.UserGroupFacade;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = {"/edituser", "/editgroup"})
public class EditServlet extends HttpServlet {

    @EJB
    private UserGroupFacade userGroupFacade;

    @EJB
    private UserFacade userFacade;

    private String id = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            id = "id".equals(param) ? request.getParameter(param) : id;
        }

        RequestDispatcher dispatcher = null;
        if ("/edituser".equals(userPath)){
            request.setAttribute("user", userFacade.getById(Integer.parseInt(id)));
            request.setAttribute("groups", userGroupFacade.getAll());

            dispatcher = request.getRequestDispatcher("/views/editUser.jsp");
        } else if ("/editgroup".equals(userPath)){
            request.setAttribute("group", userGroupFacade.getById(Integer.parseInt(id)));
            dispatcher = request.getRequestDispatcher("/views/editGroup.jsp");
        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        if ("/edituser".equals(userPath)){
            UserEntity user = userFacade.getById(Integer.parseInt(id));
            UserGroupEntity group = userGroupFacade.getById(Integer.parseInt(request.getParameter("group_user")));
            user.setFirstName(request.getParameter("first_name"));
            user.setLastName(request.getParameter("last_name"));
            user.setUserGroup(group);
            userFacade.edit(user);
        } else if ("/editgroup".equals(userPath)){
            UserGroupEntity group = userGroupFacade.getById(Integer.parseInt(id));
            group.setName(request.getParameter("group"));
            userGroupFacade.edit(group);
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }

}
