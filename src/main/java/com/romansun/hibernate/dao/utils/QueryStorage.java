package com.romansun.hibernate.dao.utils;

public abstract class QueryStorage {
	/* Bind Variables */
	public static final String VISITOR_BIND 		= "visitor_id";
	public static final String ASSOCIATION_BIND 	= "asc_id";
	public static final String TALON_BIND 			= "talon_id";
	public static final String MASK_BIND			= "mask";
	public static final String CNT_LUNCH_BIND 		= "cnt_lunch";
	public static final String CNT_DINNER_BIND		= "cnt_dinner";
	
	/* Visitor Queries */
	public static final String GET_VISITORS_BY_ASSOCIATION = 
			"SELECT v " +
			"  FROM Visitor v " +
			"  WHERE association = :" + ASSOCIATION_BIND;
	public static final String GET_VISITORS_BY_MASK = 
			"SELECT v " +
			"  FROM Visitor v " +
			"  WHERE (LOWER(lastname) || ' ' || LOWER(firstname) || ' ' || LOWER(middlename)) like :" + MASK_BIND;
	public static final String GET_VISITORS_BY_MASK_AND_ASSOCIATION = 
			GET_VISITORS_BY_MASK +
			"  AND v.association = :" + ASSOCIATION_BIND;
	public static final String GET_VISITORS_CNT =
			"SELECT count(1) from Visitor";
	
	/* Talon Queries */
	public static final String UPDATE_TALON_BY_ID =
			"UPDATE Talon t " +
			"  SET t.count_lunch = :" + CNT_LUNCH_BIND + ", t.count_dinner = :" + CNT_DINNER_BIND +
			"  WHERE t.talon_id = :" + TALON_BIND;
	public static final String RESET_ALL_TALONS = 
			"UPDATE Talon t" +
			"  SET t.count_lunch = 0, t.count_dinner = 0";
	public static final String RESET_LUNCHES_BY_TALON = 
			"UPDATE Talon t " +
			"  SET t.count_lunch = 0" + 
			"  WHERE t.talon_id = :" + TALON_BIND;
	public static final String RESET_DINNERS_BY_TALON = 
			"UPDATE Talon t " +
			"  SET t.count_dinner = 0" + 
			"  WHERE t.talon_id = :" + TALON_BIND;
	public static final String GET_COUNT_OF_LUNCHES = 
			"SELECT SUM(t.count_lunch) FROM Talon t";
	public static final String GET_COUNT_OF_DINNERS = 
			"SELECT SUM(t.count_dinner) FROM Talon t";
	
	/* Association Queries */
	public static final String GET_COUNT_OF_ASSOCIATIONS = 
			"SELECT count(1) FROM Association a";
}
