package model;

import Persistance.connectDB;

public class Remove {

	public static void removeCountLunch(int visitor_id){
		connectDB.removeLaunchForVisitor(visitor_id);
	}
	
	public static void removeCountDinner(int visitor_id){
		connectDB.removeDinnerForVisitor(visitor_id);
	}
	
	public static void removeVisitor(int visitor_id){
		connectDB.removeVisitorByID(visitor_id);
	}
	
	public static void removeAllLunch(){
		connectDB.removeLaunchForAll();
	}
	
	public static void removeAllDinner(){
		connectDB.removeDinnerForAll();
	}
}
