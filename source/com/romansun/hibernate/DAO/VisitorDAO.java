package com.romansun.hibernate.DAO;

import java.util.Collection;

import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Visitor;

public interface VisitorDAO extends EntityDAO<Visitor> {
	
	public Collection<Visitor> getVisitorsByCriteria(Association a, String mask) 	throws Exception;
	
	public int getCountVisitors() 													throws Exception;
}
