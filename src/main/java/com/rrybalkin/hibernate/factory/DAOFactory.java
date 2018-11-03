package com.rrybalkin.hibernate.factory;

import com.rrybalkin.hibernate.dao.AssociationDAO;
import com.rrybalkin.hibernate.dao.TalonDAO;
import com.rrybalkin.hibernate.dao.VisitorDAO;
import com.rrybalkin.hibernate.dao.impl.AssociationDAOImpl;
import com.rrybalkin.hibernate.dao.impl.VisitorDAOImpl;
import com.rrybalkin.hibernate.dao.impl.TalonDAOImpl;

public class DAOFactory {

    private static VisitorDAO visitorDAO = null;
    private static AssociationDAO associationDAO = null;
    private static TalonDAO talonDAO = null;
    private static DAOFactory instance = null;

    private DAOFactory() { }

    public static DAOFactory getInstance() {
        synchronized (DAOFactory.class) {
            if (instance == null) {
                instance = new DAOFactory();
            }
        }
        return instance;
    }

    public VisitorDAO getVisitorDAO() {
        synchronized (this) {
            if (visitorDAO == null) {
                visitorDAO = new VisitorDAOImpl();
            }
        }
        return visitorDAO;
    }

    public AssociationDAO getAssociationDAO() {
        synchronized (this) {
            if (associationDAO == null) {
                associationDAO = new AssociationDAOImpl();
            }
        }
        return associationDAO;
    }

    public TalonDAO getTalonDAO() {
        synchronized (this) {
            if (talonDAO == null) {
                talonDAO = new TalonDAOImpl();
            }
        }
        return talonDAO;
    }
}
