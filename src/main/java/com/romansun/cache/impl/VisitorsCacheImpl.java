package com.romansun.cache.impl;

import java.util.*;

import org.apache.log4j.Logger;

import com.romansun.cache.EntityCache;
import com.romansun.hibernate.dao.EntityDAO;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.factory.DAOFactory;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;

// TODO refactor by using Guava Cache
public class VisitorsCacheImpl implements VisitorDAO, EntityCache<Visitor> {
    private static final Logger LOGGER = Logger.getLogger(VisitorsCacheImpl.class);

    private final EntityDAO<Visitor> dao;
    private List<Visitor> visitors;
    private Map<Long, Visitor> visitorsById;

    public VisitorsCacheImpl() {
        dao = DAOFactory.getInstance().getVisitorDAO();

        init();
    }

    @Override
    public Visitor getById(long id) {
        return visitorsById.get(id);
    }

    @Override
    public synchronized void add(Visitor v) {
        persistChange(v, "add");

        reload();
    }

    @Override
    public synchronized void update(Visitor v) {
        if (visitors.contains(v)) {
            int index = visitors.indexOf(v);
            visitors.set(index, v);

            persistChange(v, "update");
        }
    }

    @Override
    public synchronized void delete(Visitor v) {
        if (visitors.remove(v)) {
            visitorsById.remove(v.getId());
            persistChange(v, "delete");
        }
    }

    @Override
    public Collection<Visitor> getAll() {
        return visitors;
    }

    @Override
    public synchronized void reload() {
        init();
    }

    private static Collection<Visitor> filterByFio(Collection<Visitor> visitors, String mask) {
        Collection<Visitor> suitableVisitors = new ArrayList<>();
        for (Visitor v : visitors) {
            String fullFio = extractFIO(v);
            if (fullFio.toLowerCase(Locale.getDefault()).contains(mask.toLowerCase(Locale.getDefault()))) {
                suitableVisitors.add(v);
            }
        }

        return suitableVisitors;
    }

    private static Collection<Visitor> filterByAssociation(Collection<Visitor> visitors, Association asc) {
        Collection<Visitor> suitableVisitors = new ArrayList<>();
        for (Visitor v : visitors) {
            long ascId = v.getAssociation().getId();
            if (ascId == asc.getId()) {
                suitableVisitors.add(v);
            }
        }

        return suitableVisitors;
    }

    private static String extractFIO(Visitor v) {
        return v.getLastName() + " " + v.getFirstName() + " "
                + v.getMiddleName();
    }

    private void persistChange(Visitor v, String type) {
        try {
            if ("add".equalsIgnoreCase(type)) {
                dao.add(v);
            } else if ("update".equalsIgnoreCase(type)) {
                dao.update(v);
            } else if ("delete".equalsIgnoreCase(type)) {
                dao.delete(v);
            } else {
                assert false : "Type = " + type + " is undefined";
            }
        } catch (Exception e) {
            LOGGER.error("Error in process of persisting change by type = "
                    + type + " for visitor = " + v.getId() + ": ", e);
        }
    }

    private void updateIndexes(Collection<Visitor> visitors) {
        for (Visitor v : visitors) {
            long id = v.getId();
            visitorsById.put(id, v);
        }
    }

    private void init() {
        LOGGER.info("VisitorsCache loading ...");
        try {
            visitors = Collections.synchronizedList(new ArrayList<>(DAOFactory.getInstance().getVisitorDAO().getAll()));
            visitorsById = Collections.synchronizedMap(new HashMap<Long, Visitor>(visitors.size()));

            updateIndexes(visitors);

            LOGGER.info(visitors.size()
                    + " visitors have been successfully loaded");
        } catch (Exception e) {
            LOGGER.error("In process of initializing VisitorsCache has been occurred error: ", e);
        }
    }

    @Override
    public Collection<Visitor> getVisitorsByCriteria(Association asc, String mask) throws Exception {
        Collection<Visitor> suitableVisitors;

        if (asc != null && mask != null && mask.length() != 0) {
            Collection<Visitor> subVisitors = filterByAssociation(this.visitors, asc);
            suitableVisitors = filterByFio(subVisitors, mask);
        } else if (asc != null) {
            suitableVisitors = filterByAssociation(this.visitors, asc);
        } else {
            suitableVisitors = filterByFio(this.visitors, mask);
        }

        return suitableVisitors;
    }

    @Override
    public long getCountVisitors(Association association) throws Exception {
        return filterByAssociation(this.visitors, association).size();
    }
}
