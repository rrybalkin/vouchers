package com.romansun.hibernate.factory;

import com.romansun.hibernate.dao.AssociationDAO;
import com.romansun.hibernate.dao.TalonDAO;
import com.romansun.hibernate.dao.VisitorDAO;
import com.romansun.hibernate.dao.impl.AssociationDAOImpl;
import com.romansun.hibernate.dao.impl.VisitorDAOImpl;
import com.romansun.hibernate.dao.impl.TalonDAOImpl;

public class DAOFactory {
	
	private static VisitorDAO visitorDAO = null;
	private static AssociationDAO associationDAO = null;
	private static TalonDAO talonDAO = null;
	private static DAOFactory instance = null;
	private DAOFactory() {}
	
	public static DAOFactory getInstance() {
		if (instance == null){
		      instance = new DAOFactory();
		    }
		    return instance;
	}
	
	public VisitorDAO getVisitorDAO() {
		if (visitorDAO == null) {
			visitorDAO = new VisitorDAOImpl();
		}
		return visitorDAO;
	}

	public AssociationDAO getAssociationDAO() {
		if (associationDAO == null) {
			associationDAO = new AssociationDAOImpl();
		}
		return associationDAO;
	}

	public TalonDAO getTalonDAO() {
		if (talonDAO == null) {
			talonDAO = new TalonDAOImpl();
		}
		return talonDAO;
	}
}
