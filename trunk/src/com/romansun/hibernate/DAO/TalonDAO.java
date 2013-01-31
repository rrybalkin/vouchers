package com.romansun.hibernate.DAO;

import java.sql.SQLException;

import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public interface TalonDAO {
	public void addTalon(Talon talon) throws SQLException;
	public void updateTalonForVisitor(Visitor visitor, Talon newTalon) throws SQLException;
	public void deleteTalon(Talon talon) throws SQLException;
}
