package com.romansun.gui.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import org.apache.log4j.Logger;

import com.romansun.config.Configuration;
import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;

public class SettingsWindowController extends AbstractController implements Initializable {
	private final static Logger LOG = Logger.getLogger(SettingsWindowController.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle resourcebundle) {
		try {
			filltbSettingsWithSettings();
			
			// Setting property for editing data to Columns
			tcKey.setCellFactory(TextFieldTableCell.forTableColumn());
			tcKey.setCellValueFactory(new PropertyValueFactory<SettingRow,String>("key"));
			tcKey.setOnEditCommit(
				    new EventHandler<CellEditEvent<SettingRow, String>>() {
				        public void handle(CellEditEvent<SettingRow, String> t) {
				            ((SettingRow) t.getTableView().getItems().get(
				                t.getTablePosition().getRow())
				                ).setKey(t.getNewValue());
				        }
				    }
				);
			tcValue.setCellValueFactory(new PropertyValueFactory<SettingRow,String>("value"));
			tcValue.setCellFactory(TextFieldTableCell.forTableColumn());
			tcValue.setOnEditCommit(
			    new EventHandler<CellEditEvent<SettingRow, String>>() {
			        public void handle(CellEditEvent<SettingRow, String> t) {
			            ((SettingRow) t.getTableView().getItems().get(
			                t.getTablePosition().getRow())
			                ).setValue(t.getNewValue());
			        }
			    }
			);
		} catch (Exception e) {
			LOG.error("Error while initialize SettingsWindowController: ", e);
		}
	}
	
	protected void filltbSettingsWithSettings() throws FileNotFoundException, UnsupportedEncodingException {
		File settings = new File(Configuration.PATH_TO_CONFIG_FILE);
		if (settings.exists()) {
			ObservableList<SettingRow> dataTable = FXCollections.observableArrayList();
			
			Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(settings), "UTF-8"));
			while(scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line != null && line.length() > 0) {
					String[] setting = line.split("=");
					if (setting.length != 2) {
						LOG.debug("Setting = '" + line + "' is incorrect.");
						continue;
					}
					
					String settingName = setting[0].trim();
					String settingValue = setting[1].trim();
					dataTable.add(new SettingRow(settingName, settingValue));
				} else {
					LOG.debug("Line = '" + line + "' cannot be processed.");
				}
			}
			scanner.close();
			
			tbSettings.setItems(dataTable);
		} else {
			String msg = "Error while reading file with settings by path = " + Configuration.PATH_TO_CONFIG_FILE + ": ";
			LOG.error(msg);
			Dialog.showError(msg);
		}
	}
	
	@FXML
	protected void saveChanges() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(new File(Configuration.PATH_TO_CONFIG_FILE), "UTF-8");
		// getting data from tbSettings
		writer.println(""); // need because in the first line when properties are reading a sign '?' appears
		Iterator<SettingRow> iterator = tbSettings.getItems().iterator();
		while (iterator.hasNext()) {
			SettingRow row = iterator.next();
			String settingName = row.getKey();
			String settingValue = row.getValue();
			
			writer.println(settingName + " = " + settingValue);
		}
		
		writer.close();
	}
	
	@FXML
	protected void cancelChanges() throws FileNotFoundException, UnsupportedEncodingException {
		filltbSettingsWithSettings();
	}
	
	@FXML //Method for removing row from tbSettings
	private void removeRowAction(ActionEvent event) {
		int index = tbSettings.getSelectionModel().getSelectedIndex();
		if (index != -1) {
			tbSettings.getItems().remove(index);
		}
	}
	
	@FXML//Method for adding new row in tbSettings
	private void addRowAction(ActionEvent event) {
		String key = txtKey.getText();
		String value = txtValue.getText();
		
		tbSettings.getItems().add(new SettingRow(key, value));
		
		txtKey.clear();
		txtValue.clear();
	}
	
	@FXML//TableView View for Settings
	private TableView<SettingRow> tbSettings;
	@SuppressWarnings("rawtypes")
	@FXML//tbSettings Column of tbSettings "Key"
	private TableColumn tcKey;
	@SuppressWarnings("rawtypes")
	@FXML//tbSettings Column of tbSettings "Value"
	private TableColumn tcValue;
	
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	
	@FXML//Button "Add" for adding new row in tbSettings
	private Button btnAddRow;
	@FXML//Button "X" for remove row from tbSettings
	private Button btnRemoveRow;
	@FXML//Text Field for adding Key
	private TextField txtKey;
	@FXML//Text Field for adding Value
	private TextField txtValue;
	
	public class SettingRow {
		private final SimpleStringProperty key;
		private final SimpleStringProperty value;
		
		public SettingRow(String key, String value) {
			this.key = new SimpleStringProperty(key);
			this.value = new SimpleStringProperty(value);
		}
		
		public String getKey() {
			return key.get();
		}

		public void setKey(String key) {
			this.key.set(key);
		}

		public String getValue() {
			return value.get();
		}

		public void setValue(String value) {
			this.value.set(value);
		}	
	}

}
