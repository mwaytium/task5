package com.nc.JPAmodel;

import com.nc.entity.UserGroupEntity;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;


@Stateless
public class UserGroupFacade extends AbstractFacade<UserGroupEntity> {

    @PersistenceContext(unitName="oracleDS")
    EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserGroupEntity getById(int id) {
        Query query = em.createQuery("from UserGroupEntity where id = :id");
        query.setParameter("id", id);
        return (UserGroupEntity) query.getSingleResult();
    }

    public List<UserGroupEntity> getByName(String name) {
        Query query = em.createQuery("from UserGroupEntity where name like :name");
        query.setParameter("name", name);
        return (List<UserGroupEntity>) query.getResultList();
    }

    public List<UserGroupEntity> getAll() {
        return em.createQuery("from UserGroupEntity order by id").getResultList();
    }

    public UserGroupFacade() {
        super(UserGroupEntity.class);
    }
}
