package com.romansun.hibernate.dao;

import java.util.Collection;

import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;

public interface VisitorDAO extends EntityDAO<Visitor> {
	
	public Collection<Visitor> getVisitorsByCriteria(Association a, String mask) 	throws Exception;
	
	public int getCountVisitors() 													throws Exception;
}
