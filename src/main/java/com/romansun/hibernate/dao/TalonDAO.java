package com.romansun.hibernate.dao;

import com.romansun.hibernate.entity.Talon;

public interface TalonDAO extends EntityDAO<Talon> {
	
	void resetAllTalons() 				throws Exception;
	
	void resetLunchById(long talon_id) 	throws Exception;
	
	void resetDinnerById(long talon_id) 	throws Exception;
	
	int getCountLunches() 				throws Exception;
	
	int getCountDinners() 				throws Exception;
}
