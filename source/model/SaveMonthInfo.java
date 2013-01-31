package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import core.Tools;

public class SaveMonthInfo {
	private static final String NAME_DIRECTORY_FOR_SAVE = "Прошлые отчеты";
	private static String error = null;
	private static File saveFile = null;
	
	public static boolean addInfoInFile(String fileName, Map<String, String> info) {
		try {
			if (createNewFile(fileName)) {
				FileWriter writer = new FileWriter(saveFile, false);
				
				StringBuffer header = new StringBuffer(1000);
				header.append("Файл создан ");
				header.append((new SimpleDateFormat("dd-MMM-yy G hh:mm aaa")).format(Calendar.getInstance().getTime()));
				header.append("\r\n\r\nФИО посетителя | Колличество обедов | Колличество ужинов");
				writer.write(header.toString());
				
				Set<String> keys = info.keySet();
				for (String FIO : Tools.sortOfAlphabet(keys)) {
					StringBuffer line = new StringBuffer(1000);
					line.append("\r\n");
					line.append(FIO);
					line.append(" | ");
					line.append(info.get(FIO).replace(";", " | "));
					
					writer.write(line.toString());
				}
				
				writer.close();
				return true;
			}
			else 
				return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static String getError()	{
		return error;
	}

	private static boolean createNewFile(String name) throws IOException {
		name = NAME_DIRECTORY_FOR_SAVE + "\\" + name + ".txt";
		File directoryForSave = new File(NAME_DIRECTORY_FOR_SAVE);
		if (!directoryForSave.exists()) {
			if (!directoryForSave.mkdir()) {
				error = "Папка для сохранения "
						+ NAME_DIRECTORY_FOR_SAVE
						+ " не была найдена! При ее создании возникли проблемы!";
				return false;
			}
		}

		saveFile = new File(name);
		if (saveFile.exists()) {
			error = "Файл с таким именем уже существует!";
			return false;
		}
		if (saveFile.createNewFile())
			return true;
		else {
			error = "При создании нового файла возникли проблемы! Файл + "
					+ name + " не создан.";
			return false;

		}
	}
}
