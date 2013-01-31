package com.romansun.hibernate.DAO;

import java.sql.SQLException;
import java.util.Collection;

import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Visitor;

public interface VisitorDAO {
	public void addVisitor(Visitor visitor) throws SQLException;
	public void updateVisitor(Long visitor_id, Visitor newVisitor) throws SQLException;
	public Visitor getVisitorById(Long visitor_id) throws SQLException;
	public Collection<Visitor> getAllVisitors() throws SQLException;
	public Collection<Visitor> getVisitorsByMask(String mask, Association association) throws SQLException;
	public void deleteVisitor(Visitor visitor) throws SQLException;
	public Collection<Visitor> getVisitorsByAssociation(Association association) throws SQLException;
	public Collection<Visitor> getVisitorsByCriteria(Association association, String mask) throws SQLException;
}
