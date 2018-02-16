package com.nc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nc.JDBCmodel.JDBC;
import com.nc.JPAmodel.UserFacade;
import com.nc.JPAmodel.UserGroupFacade;
import com.nc.entity.JDBCUserGroupEntity;
import com.nc.entity.UserEntity;
import com.nc.entity.UserGroupEntity;
import com.nc.JAXBmodel.JAXB;
import com.nc.JAXBmodel.UserListGroupWrapper;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/xml")
public class XMLServlet extends HttpServlet {

    @EJB
    private JDBC jdbc;

    @EJB
    private UserGroupFacade userGroupFacade;

    @EJB
    private UserFacade userFacade;

    @EJB
    private JAXB jaxb;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserListGroupWrapper wrapper = jaxb.unmarshall();
       if (wrapper.getGroups() == null) {
           resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/index"));
       }

        for (UserGroupEntity temp : wrapper.getGroups()) {
            boolean flag = false;
            UserGroupEntity group;

            try {
                userGroupFacade.getById(temp.getId());
            } catch (Exception e) {
                group = new UserGroupEntity(temp.getName());
                group.setUsers(temp.getUsers());

                for (int i = 0; i < group.getUsers().size(); i++) {
                    group.getUsers().get(i).setUserGroup(group);
                    group.getUsers().get(i).setId(null);
                }
                userGroupFacade.add(group);
                flag = true;
            }

            if (!flag) {
                for (int i = 0; i < temp.getUsers().size(); i++) {
                    temp.getUsers().get(i).setUserGroup(temp);
                    userFacade.edit(temp.getUsers().get(i));
                }
                jdbc.updateGroupById(temp);
            }
        }

        wrapper = new UserListGroupWrapper();
        jaxb.marshall(wrapper.getGroups());
        resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/index"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String groupName = req.getParameter("group_name");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");

        resp.setContentType("application/json");

        if (groupName.equals("") && firstName.equals("") && lastName.equals("")) {
            List<UserGroupEntity> groups = userGroupFacade.getAll();
            jaxb.marshall(groups);
            mapper.writeValue(resp.getOutputStream(), groups);
        }
        else if (firstName.equals("") && lastName.equals("")) {
            List<UserGroupEntity> groups = userGroupFacade.getByName(groupName);
            jaxb.marshall(groups);
            mapper.writeValue(resp.getOutputStream(), groups);
        }
        else {
            List<JDBCUserGroupEntity> groups = jdbc.findByUserAndGroup(groupName, firstName, lastName);
            List<UserGroupEntity> newGroups = new ArrayList<>();
            List<UserEntity> newUsers = new ArrayList<>();

            groups.forEach(group -> {
                UserGroupEntity tempGroup = new UserGroupEntity(group.getName());
                tempGroup.setId(group.getId());

                group.getUsers().forEach(user -> {
                    UserEntity tempUser = new UserEntity(user.getFirstName(), user.getLastName());
                    tempUser.setId(user.getId());
                    newUsers.add(tempUser);
                });
                tempGroup.setUsers(newUsers);

                newGroups.add(tempGroup);
            });
            jaxb.marshall(newGroups);

            mapper.writeValue(resp.getOutputStream(), groups);
        }
    }
}
