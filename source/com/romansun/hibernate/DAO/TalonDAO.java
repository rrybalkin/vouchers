package com.romansun.hibernate.DAO;

import java.util.Collection;

import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public interface TalonDAO {
	public void addTalon(Talon talon) throws Exception;
	public Collection<Talon> getAllTalons() throws Exception;
	public void updateTalonForVisitor(Visitor visitor, Talon newTalon) throws Exception;
	public void deleteTalon(Talon talon) throws Exception;
	public void resetAllTalons() throws Exception;
	public void resetLunchById(Long talon_id) throws Exception;
	public void resetDinnerById(Long talon_id) throws Exception;
}
