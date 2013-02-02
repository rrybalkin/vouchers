package com.romansun.gui.controller.impl;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public class SecondTabController extends AbstractController implements Initializable {
	
	private static Observable observable = new Observable() {
		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
		}
	};
	
	public static void addObserver(Observer o) {
		observable.addObserver(o);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGroups();
	}
	
	@FXML
	private TextField txtLastname;
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtMiddlename;
	@FXML
	private ComboBox<Association> cbGroup;
	@FXML
	private TextArea taVisitorDescription;
	@FXML
	private TextField txtGroupName;
	@FXML
	private TextArea taGroupDescription;
	@FXML
	private ComboBox<Association> cbDelGroups;
	@FXML
	private Label lblDelGroup;
	
	@FXML
	protected void addVisitor(ActionEvent event) {
		Visitor visitor = new Visitor();
		String lastname = txtLastname.getText();
		System.out.println(lastname);
		String firstname = txtFirstname.getText();
		System.out.println(firstname);
		String middlename = txtMiddlename.getText();
		System.out.println(middlename);
		Association group = cbGroup.getValue();
		String description = taVisitorDescription.getText();
		
		if (!lastname.isEmpty() && !firstname.isEmpty() && !middlename.isEmpty()) {
			visitor.setLastname(lastname);
			visitor.setFirstname(firstname);
			visitor.setMiddlename(middlename);
			visitor.setAssociation(group);
			visitor.setDescription(description);
			
			Talon talon = new Talon();
			talon.setCount_dinner(0);
			talon.setCount_lunch(0);
			visitor.setTalon(talon);
			try {
				dao.getTalonDAO().addTalon(talon);
				dao.getVisitorDAO().addVisitor(visitor);
				Dialog.showInfo("Новый посетитель успешно добавлен!");
				observable.notifyObservers();
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{
				reset("visitor");
			}
		}else{
			Dialog.showError("Необходимо заполнить обзательные поля,\nпомеченные звездочкой *!");
		}
	}
	
	@FXML
	protected void addGroup(ActionEvent event) {
		Association group = new Association();
		String name = txtGroupName.getText();
		String description = taGroupDescription.getText();
		if(!name.isEmpty()) {
			group.setName(name);
			group.setDescription(description);	
			try {
				dao.getAssociationDAO().addAssociation(group);
				Dialog.showInfo("Новая группа успешно добавлена!");
				observable.notifyObservers();
				loadGroups();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				reset("group");
			}
		}
		else {
			Dialog.showError("Необходимо заполнить обзательные поля,\nпомеченные звездочкой *!");
		}
	}
	
	@FXML
	protected void deleteGroup(ActionEvent event) {
		Association delGroup = cbDelGroups.getValue();
		if (delGroup != null) {
			try {
				dao.getAssociationDAO().deleteAssociation(delGroup);
				Dialog.showInfo("Группа " + delGroup.getName() + " была успешно удалена!");
				observable.notifyObservers();
				loadGroups();
			}catch (SQLException e){
				e.printStackTrace();
			}finally{
				lblDelGroup.setText("");
			}
		} else {
			Dialog.showError("Необходимо выбрать группу для удаления!");
		}
	}
	
	@FXML
	protected void chooseDelGroup() {
		Association delGroup = cbDelGroups.getValue();
		if (delGroup != null) {
			lblDelGroup.setText("Удаляемая группа: \"" + delGroup.getName() + "\"");
		}
	}
	
	// Reset text in all fields
	private void reset(String type) {
		if (type.equals("visitor")) {
			txtLastname.clear();
			txtFirstname.clear();
			txtMiddlename.clear();
			cbGroup.getSelectionModel().selectFirst();
			taVisitorDescription.clear();
		} else {
			txtGroupName.clear();
			taGroupDescription.clear();
		}
	}
	
	private void loadGroups() {
		try {
			Collection<Association> associations = dao.getAssociationDAO().getAllAssociations();
			Association withoutGroup = null;
			for (Association a : associations) {
				if (a.getId().equals(0L))
					withoutGroup = a;
			}
			cbGroup.getItems().clear();
			cbGroup.getItems().setAll(associations);
			cbGroup.getSelectionModel().select(withoutGroup);
			
			cbDelGroups.getItems().clear();
			associations.remove(withoutGroup);
			cbDelGroups.getItems().addAll(associations);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
