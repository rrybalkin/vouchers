package com.romansun.hibernate.factory;

import com.romansun.hibernate.DAO.AssociationDAO;
import com.romansun.hibernate.DAO.TalonDAO;
import com.romansun.hibernate.DAO.VisitorDAO;
import com.romansun.hibernate.DAO.impl.AssociationDAOImpl;
import com.romansun.hibernate.DAO.impl.VisitorDAOImpl;
import com.romansun.hibernate.DAO.impl.TalonDAOImpl;

public class DAOFactory {
	
	private static VisitorDAO visitorDAO = null;
	private static AssociationDAO associationDAO = null;
	private static TalonDAO talonDAO = null;
	private static DAOFactory instance = null;
	
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
