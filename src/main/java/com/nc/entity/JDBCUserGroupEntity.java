package com.nc.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class JDBCUserGroupEntity {

    private Integer id;
    private String name;

    @JsonManagedReference
    private List<JDBCUserEntity> users = new ArrayList<>();

    public JDBCUserGroupEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public JDBCUserGroupEntity(Integer id, String name, JDBCUserEntity user) {
        this.id = id;
        this.name = name;
        this.users.add(user);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JDBCUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<JDBCUserEntity> users) {
        this.users = users;
    }
}
