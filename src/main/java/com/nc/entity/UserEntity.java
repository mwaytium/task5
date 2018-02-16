package com.nc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table (name = "USERS")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "userId")
    @Id
    @NotNull
    @GeneratedValue(generator = "USERS_ID_SEQ")
    @SequenceGenerator (name = "USERS_ID_SEQ", sequenceName = "USERS_ID_SEQ", allocationSize = 1)
    private Integer id;

    @XmlElement(name = "firstName")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @XmlElement(name = "lastName")
    @Column(name = "LAST_NAME")
    private String lastName;

    @XmlTransient
    @JsonBackReference
    @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UserGroupEntity userGroup;

    public UserEntity() {
    }

    public UserEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserGroupEntity getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupEntity userGroup) {
        this.userGroup = userGroup;
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
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", firstName=" + firstName + ", lastName="
                + lastName + ", userGroup=" + userGroup + "]";
    }
}
