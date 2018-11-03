package com.rrybalkin.hibernate.dao;

import java.util.Collection;

import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.entity.Visitor;

public interface VisitorDAO extends EntityDAO<Visitor> {
	
	Collection<Visitor> getVisitorsByCriteria(Association a, String mask) 	throws Exception;
	
	long getCountVisitors(Association a) 									throws Exception;
}
