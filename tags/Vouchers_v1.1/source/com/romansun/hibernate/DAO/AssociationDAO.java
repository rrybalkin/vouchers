package com.romansun.hibernate.DAO;

import java.util.Collection;

import com.romansun.hibernate.logic.Association;

public interface AssociationDAO {
	public void addAssociation(Association association) throws Exception;
	public void updateAssociation(Long association_id, Association newAssociation) throws Exception;
	public Association getAssociationById(Long association_id) throws Exception;
	public Collection<Association> getAllAssociations() throws Exception;
	public void deleteAssociation(Association association) throws Exception;
	public int getCountAssociations() throws Exception;
}
