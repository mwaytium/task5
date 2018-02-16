package com.nc.JPAmodel;

import com.nc.entity.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserFacade extends AbstractFacade<UserEntity> {

    @PersistenceContext(unitName="oracleDS")
    EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserEntity getById(int id) {
        Query query = em.createQuery("from UserEntity where id = :id");
        query.setParameter("id", id);
        return (UserEntity) query.getSingleResult();
    }

    public List<UserEntity> getByFirstNameOrLastName(String firstName, String lastName) {
        if (firstName == null) {
            firstName = "%";
        }
        if (lastName == null) {
            lastName = "%";
        }
        Query query = em.createQuery("from UserEntity where firstName like :firstName " +
                "and lastName like :lastName");
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return (List<UserEntity>) query.getSingleResult();
    }

    public List<UserEntity> getAll() {
        return em.createQuery("from UserEntity").getResultList();
    }

    public UserFacade() {
        super(UserEntity.class);
    }

}
