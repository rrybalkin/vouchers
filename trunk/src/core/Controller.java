package core;

import java.util.HashMap;

import Persistance.connectDB;

import model.FactoryInitialize;
import model.Initialize;
import model.Remove;
import model.SaveMonthInfo;
import model.Search;
import model.Update;

public class Controller {

	private static Initialize initialize = FactoryInitialize.getInit();

	// метод для поиска посетителей, удовлетворяющих маске
	public static HashMap<String, Integer> search(String mask) {
		HashMap<String, Integer> answer = new HashMap<String, Integer>();
		answer = Search.find(mask);
		return answer;
	}

	// метод для поиска всех посетителей
	public static HashMap<String, Integer> searchAll() {
		HashMap<String, Integer> answer = new HashMap<String, Integer>();
		answer = Search.findAll();
		return answer;
	}

	// метод для добавления числа обедов посетителю
	public static void addCountLunch(int visitor_id, int count) {
		Update.addCountLunch(visitor_id, count);
		initialize.addLunch(visitor_id, count);
	}

	// метод для добавления числа ужинов посетителю
	public static void addCountDinner(int visitor_id, int count) {
		Update.addCountDinner(visitor_id, count);
		initialize.addDinner(visitor_id, count);
	}

	// метод для обнуления всех обедов
	public static void removeLunchs() {
		Remove.removeAllLunch();
		initialize.removeAllCountLunch();
	}

	// метод для обнуления всех ужинов
	public static void removeDinners() {
		Remove.removeAllDinner();
		initialize.removeAllCountDinner();
	}

	// метод для обнуления числа обедов у посетителя
	public static void removeCountLunch(int visitor_id) {
		Remove.removeCountLunch(visitor_id);
		initialize.removeLunch(visitor_id);
	}

	// метод для обнуления числа ужинов у посетителя
	public static void removeCountDinner(int visitor_id) {
		Remove.removeCountDinner(visitor_id);
		initialize.removeDinner(visitor_id);
	}

	// метод для удаления посетителя
	public static void removeVisitor(int visitor_id) {
		Remove.removeVisitor(visitor_id);
		initialize.removeVisitor(visitor_id);
	}

	// метод для добавления нового посетителя
	public static void addNewVisitor(String firstname, String lastname,
			String middlename) {
		Update.addNewVisitor(firstname, lastname, middlename);
		initialize.addVisitor(firstname, lastname, middlename);
	}

	// метод для получения информации о посетителе по его id
	public static String[] getInfoByID(int visitor_id) {
		return Search.getInfoByID(visitor_id);
	}
	
	// метод для сохранения месячной информации
	public static boolean saveMonthInfo(String fileName) {
		HashMap<String, String> info = (HashMap<String, String>) connectDB.getInfoAboutAll();
		return SaveMonthInfo.addInfoInFile(fileName, info);
	}
	
	// метод для получения ошибки при сохранении месячного отчета
	public static String getErrorForSaveMonthInfo() {
		return SaveMonthInfo.getError();
	}
}
