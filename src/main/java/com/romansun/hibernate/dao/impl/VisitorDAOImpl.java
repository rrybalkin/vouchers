package com.romansun.hibernate.dao.impl;

import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.hibernate.factory.HibernateExecutor;
import org.hibernate.Query;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import static com.romansun.hibernate.dao.utils.QueryStorage.*;

public class VisitorDAOImpl implements VisitorDAO {

    @Override
    public void add(final Visitor v) {
        Objects.requireNonNull(v, "Visitor must not be null");
        HibernateExecutor.execute(session -> session.save(v));
    }

    @Override
    public void update(final Visitor v) {
        Objects.requireNonNull(v, "Visitor must not be null");
        HibernateExecutor.execute(session -> {
            session.update(v);
            return v;
        });
    }

    @Override
    public void delete(final Visitor v) {
        Objects.requireNonNull(v, "Visitor must not be null");
        HibernateExecutor.execute(session -> {
            session.delete(v);
            return v;
        });
    }

    @Override
    public Visitor getById(final long id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID must be positive");

        return HibernateExecutor.execute(session -> session.load(Visitor.class, id));
    }

    @Override
    public Collection<Visitor> getAll() {
        return HibernateExecutor.execute(session -> session.createCriteria(Visitor.class).list());
    }

    @Override
    public Collection<Visitor> getVisitorsByCriteria(final Association a, final String mask) {
        return HibernateExecutor.execute(session -> {
            Query query;
            if (a != null && (mask != null && !mask.isEmpty())) {
                query = session
                        .createQuery(GET_VISITORS_BY_MASK_AND_ASSOCIATION)
                        .setString(MASK_BIND, "%" + mask.toLowerCase(Locale.getDefault()) + "%")
                        .setLong(ASSOCIATION_BIND, a.getId());
            } else if (a != null) {
                query = session
                        .createQuery(GET_VISITORS_BY_ASSOCIATION)
                        .setLong(ASSOCIATION_BIND, a.getId());
            } else if (mask != null && !mask.isEmpty()) {
                query = session
                        .createQuery(GET_VISITORS_BY_MASK)
                        .setString(MASK_BIND, "%" + mask.toLowerCase(Locale.getDefault()) + "%");
            } else {
                throw new IllegalArgumentException("Association or mask shouldn't be null");
            }

            return (Collection<Visitor>) query.list();
        });
    }

    @Override
    public long getCountVisitors(Association association) {
        return HibernateExecutor.execute(
                session -> (Long) session
                        .createQuery(GET_VISITORS_CNT_BY_ASSOCIATION)
                        .setLong(ASSOCIATION_BIND, association.getId())
                        .uniqueResult());
    }
}
