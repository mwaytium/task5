package com.nc.JAXBmodel;

import com.nc.entity.UserGroupEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "groups")
public class UserListGroupWrapper {

    @XmlElement(name = "group")
    private List<UserGroupEntity> groups;

    public List<UserGroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroupEntity> groups) {
        this.groups = groups;
    }
}
