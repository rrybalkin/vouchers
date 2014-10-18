package com.romansun.hibernate.DAO;

import com.romansun.hibernate.logic.Talon;

public interface TalonDAO extends EntityDAO<Talon> {
	
	public void resetAllTalons() 				throws Exception;
	
	public void resetLunchById(long talon_id) 	throws Exception;
	
	public void resetDinnerById(long talon_id) 	throws Exception;
	
	public int getCountLunches() 				throws Exception;
	
	public int getCountDinners() 				throws Exception;
}
