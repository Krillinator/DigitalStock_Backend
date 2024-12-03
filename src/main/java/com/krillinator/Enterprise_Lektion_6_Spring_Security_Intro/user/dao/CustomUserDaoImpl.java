package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.dao;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomUserDaoImpl implements ICustomUserDao {

    private final EntityManager entityManager;

    @Autowired
    public CustomUserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CustomUser> filterAllUsersByTaskId(Long taskId) {

        // JPQL - Documentation
        // SELECT <entity or attribute> FROM <entity> <alias> [WHERE <condition>]
        // SELECT - Defines an attribute or instance of an entity that is defined in the FROM clause
        // FROM - Defines the Entity + Alias
        // JOIN - Defines a combination of two tables
        // WHERE - Defines a filter
        // colon - Defines parameter placeholder (for dynamic values)
        TypedQuery<CustomUser> typedQuery = entityManager.createQuery(
                """
                    SELECT u
                    FROM CustomUser u
                    JOIN u.taskList t
                    WHERE t.id = :taskId
                """,
                CustomUser.class
        );
        typedQuery.setParameter("taskId", taskId);

        return typedQuery.getResultList();
    }



}
