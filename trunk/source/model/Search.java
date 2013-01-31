package model;

import java.util.HashMap;

public class Search {

	public static Initialize initialize = FactoryInitialize.getInit();

	public static HashMap<String, Integer> findAll() {
		HashMap<String, Integer> answer = new HashMap<String, Integer>();
		for (int key : initialize.info.keySet()) {
			answer.put(initialize.info.get(key)[0], key);
		}
		return answer;
	}

	public static HashMap<String, Integer> find(String mask) {
		HashMap<String, Integer> answer = new HashMap<String, Integer>();
		for (int key : initialize.info.keySet())
			if (initialize.info.get(key)[0].toLowerCase().contains(
					mask.toLowerCase()))
				answer.put(initialize.info.get(key)[0], key);
		return answer;
	}

	// метод для получения информации о посетителе (ФИО, колличество обедов,
	// колличество ужинов)
	public static String[] getInfoByID(int visitor_id) {
		String[] answer = new String[3];
		// ФИО
		answer[0] = initialize.info.get(visitor_id)[0];
		// колличество обедов
		answer[1] = initialize.info.get(visitor_id)[1];
		// колличетсво ужинов
		answer[2] = initialize.info.get(visitor_id)[2];

		return answer;
	}

	// метод для получения информации о всех посетителях (число посетителей,
	// колличество обедов, колличество ужинов)
	public static String[] getInfoAboutAll() {
		int countAllLaunch = 0;
		int countAllDinner = 0;
		int countVisitors = 0;
		
		countVisitors = initialize.info.size();
		for (int visitor_id : initialize.info.keySet()) {
			countAllLaunch += Integer
					.parseInt(initialize.info.get(visitor_id)[1]);
			countAllDinner += Integer
					.parseInt(initialize.info.get(visitor_id)[2]);
		}
		String str1 = "" + countVisitors;
		String str2 = "" + countAllLaunch;
		String str3 = "" + countAllDinner;

		return new String[] { str1, str2, str3 };
	}
}
