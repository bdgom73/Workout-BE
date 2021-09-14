package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Routine;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Routine> findSearchTitle(List<String> lists) {
        StringBuilder query = new StringBuilder("SELECT b, ");
        query.append(" as score FROM Routine b WHERE b.share = true and ");
        for (int i = 0; i < lists.size(); i++) {
            if (i == (lists.size() - 1)) {
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' where b.share = true");
            } else {
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' or");
            }

        }

        query.append(" ORDER BY ");
        for (int i = 0; i < lists.size(); i++) {
            if (i == (lists.size() - 1)) {
                query.append(" CASE WHEN (b.title LIKE '%").append(lists.get(i)).append("%') THEN 1 ELSE 0 END");
            } else {
                query.append(" CASE WHEN (b.title LIKE '%").append(lists.get(i)).append("%') THEN 1 ELSE 0 END + ");
            }
        }
        query.append(" desc ");
        return em.createQuery(query.toString(), Routine.class)
                .getResultList();
    }

    public List<Routine> findSearchTitle(List<String> lists, PageRequest pageRequest) {
        StringBuilder query = new StringBuilder("SELECT b, ");
        query.append(" as score FROM Routine b WHERE b.share = true and ");
        for (int i = 0; i < lists.size(); i++) {
            if (i == (lists.size() - 1)) {
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' where b.share = true");
            } else {
                query.append(" b.title LIKE '%").append(lists.get(i)).append("%' or");
            }

        }
        if (pageRequest.getSort().isSorted()) {
            query.append(" ORDER BY ").append(
                    pageRequest.getSort().toString().replace(":", ""));
        } else {
            query.append(" ORDER BY ");
            for (int i = 0; i < lists.size(); i++) {
                if (i == (lists.size() - 1)) {
                    query.append(" CASE WHEN (b.title LIKE '%").append(lists.get(i)).append("%') THEN 1 ELSE 0 END");
                } else {
                    query.append(" CASE WHEN (b.title LIKE '%").append(lists.get(i)).append("%') THEN 1 ELSE 0 END + ");
                }
            }
            query.append(" desc ");
        }
        TypedQuery<Routine> query1 = em.createQuery(query.toString(), Routine.class);
        query1.setFirstResult((int) pageRequest.getOffset());
        query1.setMaxResults(pageRequest.getPageSize());
        return query1.getResultList();

    }

    public List<Routine> findByKeyword(List<String> keywords) {
        StringBuilder query = new StringBuilder("SELECT b, ");
        query.append(" as score FROM Routine b WHERE b.share = true and ");
        for (int i = 0; i < keywords.size(); i++) {
            if (i == (keywords.size() - 1)) {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%' where b.share = true");
            } else {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%' or");
            }

        }
        query.append(" ORDER BY ");
        for (int i = 0; i < keywords.size(); i++) {
            if (i == (keywords.size() - 1)) {
                query.append(" CASE WHEN (b.title LIKE '%").append(keywords.get(i)).append("%') THEN 1 ELSE 0 END");
            } else {
                query.append(" CASE WHEN (b.title LIKE '%").append(keywords.get(i)).append("%') THEN 1 ELSE 0 END + ");
            }
        }
        query.append(" desc ");
        return em.createQuery(query.toString(), Routine.class).getResultList();
    }

    public Long countByTitle(List<String> keywords) {
        StringBuilder query = new StringBuilder("SELECT COUNT(b) FROM Routine b WHERE ");
        query.append(" b.share = true and ");
        for (int i = 0; i < keywords.size(); i++) {
            if (i == (keywords.size() - 1)) {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%'");
            } else {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%' or");
            }
        }

        return em.createQuery(query.toString(), Long.class).getSingleResult();
    }

    public List<Routine> findByKeyword(List<String> keywords, PageRequest pageRequest) {
        StringBuilder query = new StringBuilder("SELECT b ");


        query.append(" FROM Routine b WHERE b.share = true and ");
        for (int i = 0; i < keywords.size(); i++) {
            if (i == (keywords.size() - 1)) {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%'");
            } else {
                query.append(" b.title LIKE '%").append(keywords.get(i)).append("%' or");
                query.append(" b.part LIKE '%").append(keywords.get(i)).append("%' or");
            }

        }

        if (pageRequest.getSort().isSorted()) {
            query.append(" ORDER BY ").append(
                    pageRequest.getSort().toString().replace(":", ""));
        } else {
            query.append(" ORDER BY ");
            for (int i = 0; i < keywords.size(); i++) {
                if (i == (keywords.size() - 1)) {
                    query.append(" CASE WHEN (b.title LIKE '%").append(keywords.get(i)).append("%') THEN 1 ELSE 0 END");
                } else {
                    query.append(" CASE WHEN (b.title LIKE '%").append(keywords.get(i)).append("%') THEN 1 ELSE 0 END + ");
                }
            }
            query.append(" desc ");
        }

        TypedQuery<Routine> query1 = em.createQuery(query.toString(), Routine.class);
        query1.setFirstResult((int) pageRequest.getOffset());
        query1.setMaxResults(pageRequest.getPageSize());
        return query1.getResultList();
    }

}
