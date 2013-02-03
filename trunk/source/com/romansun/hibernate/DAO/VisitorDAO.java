package com.romansun.hibernate.DAO;

import java.util.Collection;

import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Visitor;

public interface VisitorDAO {
	public void addVisitor(Visitor visitor) throws Exception;
	public void updateVisitor(Long visitor_id, Visitor newVisitor) throws Exception;
	public Visitor getVisitorById(Long visitor_id) throws Exception;
	public Collection<Visitor> getAllVisitors() throws Exception;
	public Collection<Visitor> getVisitorsByMask(String mask, Association association) throws Exception;
	public void deleteVisitor(Visitor visitor) throws Exception;
	public Collection<Visitor> getVisitorsByAssociation(Association association) throws Exception;
	public Collection<Visitor> getVisitorsByCriteria(Association association, String mask) throws Exception;
}
