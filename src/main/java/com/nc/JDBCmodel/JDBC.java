package com.nc.JDBCmodel;

import com.nc.entity.JDBCUserEntity;
import com.nc.entity.JDBCUserGroupEntity;
import com.nc.entity.UserEntity;
import com.nc.entity.UserGroupEntity;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class JDBC {

    private  DataSource  ds  = null;
    private  Connection  con = null;

    public JDBC() {
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:/OracleDS");
            if (ds != null) {
                con = ds.getConnection();
            }
            Statement statement = con.createStatement();
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_TABLES where table_name='USERGROUP'; if c = 0 then execute immediate 'CREATE TABLE usergroup ( id NUMBER(10) NOT NULL, name VARCHAR2(100) NOT NULL, CONSTRAINT usergroup_pk PRIMARY KEY (id))'; end if; end;");
            statement.executeUpdate("declare c int; begin select count(*) into c from ALL_TABLES where table_name='USERS'; if c = 0 then execute immediate 'CREATE TABLE users ( id NUMBER(10) NOT NULL, first_name VARCHAR2(100), last_name VARCHAR2(100), usergroup_id NUMBER(10), CONSTRAINT user_pk PRIMARY KEY (id), CONSTRAINT fk_usergroup FOREIGN KEY (usergroup_id) REFERENCES usergroup(id))'; end if; end;");
        }
        catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String id) {
        try (final PreparedStatement statement = this.con.prepareStatement("delete from users where id = (?)")) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroupById(UserGroupEntity group) {
        try (final PreparedStatement statement = this.con.prepareStatement (
                "update usergroup set name = (?) where id = (?)" )) {
            statement.setString(1, group.getName());
            statement.setString(2, String.valueOf(group.getId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<JDBCUserGroupEntity> findByUserAndGroup(String gn, String fn, String ln) {
        gn = (gn.equals("")) ? "%" : gn;
        fn = (fn.equals("")) ? "%" : fn;
        ln = (ln.equals("")) ? "%" : ln;
        Integer id;
        Integer usergroupId;
        String firstName;
        String lastName;
        String groupName;
        List<JDBCUserGroupEntity> groups = new ArrayList<>();
        boolean flag;

        try (final PreparedStatement statement = this.con.prepareStatement(
                "select id, first_name, last_name, usergroup_id, (select name " +
                            "from usergroup " +
                            "where u.usergroup_id = id) as group_name " +
                        "from users u " +
                        "where usergroup_id in (select id " +
                            "from usergroup ug " +
                            "where ug.name like (?)) and u.first_name like (?) and u.last_name like (?)"
             )
        ) {
            statement.setString(1, gn);
            statement.setString(2, fn);
            statement.setString(3, ln);
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    flag = false;
                    id = rs.getInt("id");
                    usergroupId = rs.getInt("usergroup_id");
                    firstName = rs.getString("first_name");
                    lastName = rs.getString("last_name");
                    groupName = rs.getString("group_name");

                    if (groups.size() != 0) {
                        for (JDBCUserGroupEntity group : groups) {
                            if (group.getId() == usergroupId) {
                                group.getUsers().add(new JDBCUserEntity(id, firstName, lastName));
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (groups.size() == 0 || !flag) {
                        groups.add(new JDBCUserGroupEntity(
                                        usergroupId,
                                        groupName,
                                        new JDBCUserEntity(id, firstName, lastName)
                                )
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}
