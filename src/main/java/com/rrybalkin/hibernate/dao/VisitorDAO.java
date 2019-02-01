package com.rrybalkin.hibernate.dao;

import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.entity.Visitor;

import java.util.List;

public interface VisitorDAO extends EntityDAO<Visitor> {
	
	List<Visitor> getVisitorsByCriteria(Association a, String mask) 	throws Exception;
	
	long getCountVisitors(Association a) 									throws Exception;
}
