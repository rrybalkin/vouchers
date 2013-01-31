package com.romansun.hibernate.DAO;

import java.sql.SQLException;
import java.util.Collection;

import com.romansun.hibernate.logic.Association;

public interface AssociationDAO {
	public void addAssociation(Association association) throws SQLException;
	public void updateAssociation(Long association_id, Association newAssociation) throws SQLException;
	public Association getAssociationById(Long association_id) throws SQLException;
	public Collection<Association> getAllAssociations() throws SQLException;
	public void deleteAssociation(Association association) throws SQLException;
}
