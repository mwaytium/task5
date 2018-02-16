package com.nc.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "USERGROUP")
public class UserGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "groupId")
    @Id
    @NotNull
    @GeneratedValue(generator = "USERGROUP_ID_SEQ")
    @SequenceGenerator (name = "USERGROUP_ID_SEQ", sequenceName = "USERGROUP_ID_SEQ", allocationSize = 1)
    private Integer id;

    @XmlElement(name = "name")
    @Column(name = "NAME")
    private String name;

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    @JsonManagedReference
    @OneToMany(mappedBy = "userGroup", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<UserEntity> users;

    public UserGroupEntity() {
    }

    public UserGroupEntity(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupEntity)) {
            return false;
        }
        UserGroupEntity other = (UserGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserGroupEntity [id=" + id + ", name=" + name + ", users="
                + users + "]";
    }
}
