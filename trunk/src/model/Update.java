package model;

import Persistance.connectDB;

public class Update {
	
	public static void addCountLunch(int idVisitor, int count) {
		connectDB.setLaunchForVisitor(idVisitor, count);
	}
	
	public static void addCountDinner(int idVisitor, int count) {
		connectDB.setDinnerForVisitor(idVisitor, count);
	}
	
	public static void addNewVisitor(String firstname, String lastname, String middlename){
		String[] info = {firstname, lastname, middlename};
		connectDB.addNewVisitor(info);
	}
}
