package model;

import java.util.HashMap;

import Persistance.connectDB;

public class Initialize {
	
	Initialize(){
		initialize();
	}

	public HashMap<Integer, String[]> info = new HashMap<Integer, String[]>();

	public void initialize() {

		// поднимаем из базы всех посетителей
		String[] allVisitors = connectDB.getAllVisitors();
		
		for (int i = 0; i < allVisitors.length; i++) {
			String[] partAllVisitors = allVisitors[i].split(" ");
			String[] infoAboutVisitors = new String[3];
			// id
			int id = Integer.parseInt(partAllVisitors[0]);
			// FIO
			infoAboutVisitors[0] = partAllVisitors[1] + " "
					+ partAllVisitors[2] + " " + partAllVisitors[3];
			// count_lunch
			infoAboutVisitors[1] = partAllVisitors[4];
			// count_dinner
			infoAboutVisitors[2] = partAllVisitors[5];

			info.put(id, infoAboutVisitors);
		}
	}

	public void addLunch(int visitor_id, int count) {
		String countLunch = info.get(visitor_id)[1];
		int newCount = Integer.parseInt(countLunch) + count;
		String[] newInfo = { info.get(visitor_id)[0], newCount + "",
				info.get(visitor_id)[2] };
		info.put(visitor_id, newInfo);
	}

	public void addDinner(int visitor_id, int count) {
		String countDinner = info.get(visitor_id)[2];
		int newCount = Integer.parseInt(countDinner) + count;
		String[] newInfo = { info.get(visitor_id)[0], info.get(visitor_id)[1],
				newCount + "" };
		info.put(visitor_id, newInfo);
	}
	 
	public void removeLunch(int visitor_id) {
		String[] newInfo = { info.get(visitor_id)[0], "0", info.get(visitor_id)[2] };
		info.put(visitor_id, newInfo);
	}
	
	public void removeDinner(int visitor_id) {
		String[] newInfo = { info.get(visitor_id)[0], info.get(visitor_id)[1], "0" };
		info.put(visitor_id, newInfo);
	}

	public void removeAllCountLunch() {
		for (int key : info.keySet()) {
			String[] newInfo = { info.get(key)[0], "0", info.get(key)[2] };
			info.put(key, newInfo);
		}
	}
	
	public void removeAllCountDinner() {
		for (int key : info.keySet()) {
			String[] newInfo = { info.get(key)[0], info.get(key)[1], "0" };
			info.put(key, newInfo);
		}
	}

	public void removeVisitor(int visitor_id) {
		info.remove(visitor_id);
	}

	public void addVisitor(String firstname, String lastname,
			String middlename) {
		initialize();
	}
}
