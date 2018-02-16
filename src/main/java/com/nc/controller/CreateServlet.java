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

@WebServlet(urlPatterns = {"/createuser", "/creategroup"})
public class CreateServlet extends HttpServlet {

    @EJB
    private UserGroupFacade userGroupFacade;

    @EJB
    private UserFacade userFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        RequestDispatcher dispatcher = null;
        String userPath = request.getServletPath();
        if ("/createuser".equals(userPath)){
            request.setAttribute("groups", userGroupFacade.getAll());
            dispatcher = request.getRequestDispatcher("/views/createUser.jsp");
        } else if ("/creategroup".equals(userPath)){
            dispatcher = request.getRequestDispatcher("/views/createGroup.jsp");

        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        String userPath = request.getServletPath();
        if ("/createuser".equals(userPath)){
            UserGroupEntity group = userGroupFacade.getById(Integer.parseInt(request.getParameter("group_user")));
            UserEntity user = new UserEntity(
                    request.getParameter("first_name"),
                    request.getParameter("last_name")
            );
            user.setUserGroup(group);
            userFacade.add(user);
        } else if ("/creategroup".equals(userPath)){
            userGroupFacade.add(new UserGroupEntity(request.getParameter("group")));
        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/index"));
    }
}
