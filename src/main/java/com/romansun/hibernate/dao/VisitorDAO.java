package com.romansun.hibernate.dao;

import java.util.Collection;

import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Visitor;

public interface VisitorDAO extends EntityDAO<Visitor> {
	
	Collection<Visitor> getVisitorsByCriteria(Association a, String mask) 	throws Exception;
	
	long getCountVisitors(Association a) 									throws Exception;
}
